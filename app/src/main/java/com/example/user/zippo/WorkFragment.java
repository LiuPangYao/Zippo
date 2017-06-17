package com.example.user.zippo;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.greendao.ZI_BlogData;
import com.example.greendao.ZI_EmailData;
import com.example.user.zippo.camera.BitmapUtils;
import com.example.user.zippo.camera.CapturePhotoHelper;
import com.example.user.zippo.camera.RuleUtils;
import com.example.user.zippo.custom.DensityUtil;
import com.example.user.zippo.custom.ErrorDialog;
import com.example.user.zippo.custom.SharedPreferencesUtil;
import com.example.user.zippo.custom.XCFlowLayout;
import com.example.user.zippo.folder.FolderManager;
import com.example.user.zippo.fragment.BackHandledFragment;
import com.example.user.zippo.store.BlogDataStore;
import com.example.user.zippo.store.EmailDataStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static com.example.user.zippo.MenuActivity.RUNTIME_PERMISSION_REQUEST_CODE;

/**
 * 個人檔案
 */
public class WorkFragment extends BackHandledFragment implements View.OnClickListener
{
    private ImageView mImageViewEdit, // 大頭貼、姓名
            mImageViewEdit1, // Email、網址
            mImageViewEdit2, // 工作經驗
            mImageViewEdit3, // 教育背景
            mImageViewEdit4; // 個人成就

    private ImageView mImageViewPicture; // 取得 相簿照片 或 相機照片
    private TextView mTxtMail, mTxtBlog; // 聯絡資料使用
    private TextView mTxtName, mTxtIntroduction; // 姓名 專業簡介
    private XCFlowLayout mXCFlowLayout;

    private Dialog chooseDialog; // 照片選擇 Dialog

    SharedPreferencesUtil spUtil;

    private CapturePhotoHelper mCapturePhotoHelper;
    private File mRestorePhotoFile;
    private final static String TAG = "TAG";
    private final static String EXTRA_RESTORE_PHOTO = "extra_restore_photo";
    private static final int PHOTO = 1;
    private final static float RATIO = 0.75f;
    private Boolean _confirmCamera = false , _confirmStorage = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work, container, false);

        mImageViewEdit = (ImageView)view.findViewById(R.id.imageViewEdit);
        mImageViewEdit1 = (ImageView)view.findViewById(R.id.imageViewEdit1);
        mImageViewEdit2 = (ImageView)view.findViewById(R.id.imageViewEdit2);
        mImageViewEdit3 = (ImageView)view.findViewById(R.id.imageViewEdit3);
        mImageViewEdit4 = (ImageView)view.findViewById(R.id.imageViewEdit4);

        mImageViewPicture = (ImageView)view.findViewById(R.id.imageViewPicture);

        mImageViewEdit.setOnClickListener(this);
        mImageViewEdit1.setOnClickListener(this);
        mImageViewEdit2.setOnClickListener(this);
        mImageViewEdit3.setOnClickListener(this);
        mImageViewEdit4.setOnClickListener(this);
        mImageViewPicture.setOnClickListener(this);

        mXCFlowLayout = (XCFlowLayout) view.findViewById(R.id.flowLayout); // 標籤

        mTxtMail = (TextView) view.findViewById(R.id.txtMail);
        mTxtBlog = (TextView) view.findViewById(R.id.txtBlogAddress);

        mTxtName = (TextView) view.findViewById(R.id.txtName);
        mTxtIntroduction = (TextView) view.findViewById(R.id.txtIntroduction);

        spUtil = new SharedPreferencesUtil(getActivity(), "introduction_data");

        initMessage(); // 初始化

        FolderManager folderManager = new FolderManager(getActivity()); // 命名檔案夾，要移到 Application

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        if (mCapturePhotoHelper != null)
        {
            mRestorePhotoFile = mCapturePhotoHelper.getPhoto();
            Log.i(TAG, "onSaveInstanceState , mRestorePhotoFile: " + mRestorePhotoFile);
            spUtil.putString("PicturePath", String.valueOf(mRestorePhotoFile));
            if (mRestorePhotoFile != null)
            {
                outState.putSerializable(EXTRA_RESTORE_PHOTO, mRestorePhotoFile);
            }
        }
    }

    // 目前沒有作用
    @Override
    public void onViewStateRestored(Bundle savedInstanceState)
    {
        super.onViewStateRestored(savedInstanceState);
        Log.i(TAG, "onViewStateRestored");
        if (mCapturePhotoHelper != null)
        {
            mRestorePhotoFile = (File) savedInstanceState.getSerializable(EXTRA_RESTORE_PHOTO);
            Log.i(TAG, "onRestoreInstanceState , mRestorePhotoFile: " + mRestorePhotoFile);
            mCapturePhotoHelper.setPhoto(mRestorePhotoFile);
        }
    }

    /**
     * 初始化載入資料
     */
    public void initMessage()
    {
        EmailDataStore emailDataStore = new EmailDataStore(getActivity()); // 資料庫 有資料 帶入
        List<ZI_EmailData> data = emailDataStore.getList();

        BlogDataStore blogDataStore = new BlogDataStore(getActivity()); // 若資料庫 有資料 帶入
        List<ZI_BlogData> data1 = blogDataStore.getList();

        if(data.size() != 0)
        {
            mTxtMail.setText(data.get(0).getEmail().toString());
        }

        if(data1.size() != 0)
        {
            mTxtBlog.setText(data1.get(0).getBlog().toString());
        }

        if(!spUtil.getString("Name","").toString().trim().equals("") || !spUtil.getString("NameFirst", "").toString().trim().equals(""))
        {
            mTxtName.setText(spUtil.getString("NameFirst", "") + "\t" +spUtil.getString("Name",""));
        }

        if(!spUtil.getString("Introduction", "").toString().trim().equals(""))
        {
            mTxtIntroduction.setText(spUtil.getString("Introduction", ""));
        }

        // 載入圖片
        if(!spUtil.getString("PicturePath", "").toString().trim().equals(""))
        {
            char checkFirst ='c';

            if(spUtil.getString("PicturePath", "").toString().charAt(0) == checkFirst)
            {
                mImageViewPicture.setImageURI(Uri.parse(spUtil.getString("PicturePath", "")));
            } else {
                Bitmap bmp = BitmapFactory.decodeFile(spUtil.getString("PicturePath", ""));
                mImageViewPicture.setImageBitmap(bmp);
            }
        }

        ArrayList mTag = new ArrayList();

        if(!spUtil.getString("WorkTitle", "").toString().trim().equals(""))
        {
            mTag.add(spUtil.getString("WorkTitle", "").toString());
        }

        if(!spUtil.getString("WorkNow", "").toString().trim().equals(""))
        {
            mTag.add(spUtil.getString("WorkNow", "").toString());
        }

        if(!spUtil.getString("WorkTeach", "").toString().trim().equals(""))
        {
            mTag.add(spUtil.getString("WorkTeach", "").toString());
        }

        if(!spUtil.getString("WorkCountry", "").toString().trim().equals(""))
        {
            mTag.add(spUtil.getString("WorkCountry", "").toString());
        }

        // 載入標籤 控制標籤長寬
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams
                (
                        LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 5;
        lp.bottomMargin = 5;

        if(mTag.size() != 0)
        {
            for(int i = 0; i < mTag.size(); i ++)
            {
                TextView view = new TextView(getActivity());
                view.setText(mTag.get(i).toString());
                view.setTextColor(Color.BLACK);
                GradientDrawable background = new GradientDrawable(); // 得到随机颜色

                Random random = new Random();
                int alpha = 255;
                int red = random.nextInt(190)+30; // 0-255 30-220
                int green = random.nextInt(190)+30; // 30-220
                int blue = random.nextInt(190)+30; // 30-220
                int argb = Color.argb(alpha, red, green, blue);

                background.setColor(argb); // 设置填充颜色
                background.setCornerRadius(DensityUtil.dip2px(getActivity(), 6)); // 设置圆角半径

                view.setBackgroundDrawable(background); // 这setBackgroundDrawable(Drawable)是一个过时的方法
                view.setPadding(10, 10, 10, 10);
                mXCFlowLayout.addView(view,lp);
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

        switch (v.getId())
        {
            case R.id.imageViewEdit: // 編輯 姓名 簡介 國家
                IntroductionFragment introductionFragment = new IntroductionFragment();
                ft.add(R.id.fragment_container_working, introductionFragment);
                ft.addToBackStack("introductionFragment");
                ft.commit();
                break;

            case R.id.imageViewEdit1: // 編輯 聯絡資料
                EditConnectionFragment editConnectionFragment = new EditConnectionFragment();
                ft.add(R.id.fragment_container_working, editConnectionFragment);
                ft.addToBackStack("editConnectionFragment");
                ft.commit();
                break;

            case R.id.imageViewEdit2: // 工作經驗
                WorkHistoryFragment workHistoryFragment = new WorkHistoryFragment();
                ft.add(R.id.fragment_container_working, workHistoryFragment);
                ft.addToBackStack("workHistoryFragment");
                ft.commit();
                break;

            case R.id.imageViewEdit3: // 教育背景
                TeachFragment teachFragment = new TeachFragment();
                ft.add(R.id.fragment_container_working, teachFragment);
                ft.addToBackStack("teachFragment");
                ft.commit();
                break;

            case R.id.imageViewEdit4: // 個人成就
                PersonalFragment personalFragment = new PersonalFragment();
                ft.add(R.id.fragment_container_working, personalFragment);
                ft.addToBackStack("personalFragment");
                ft.commit();
                break;

            case R.id.imageViewPicture: // 編輯頭像

                ErrorDialog.Builder builder = new ErrorDialog.Builder(getActivity());
                chooseDialog =
                        builder.cancelTouchout(false)
                                .view(R.layout.dialog_custom_choose)
                                .widthdp(200)
                                .heightdp(150)
                                .style(R.style.Dialog)
                                .addViewOnclick(R.id.txtCamera, new View.OnClickListener() // 點選相機
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                        {
                                            // Android M 处理 Runtime Permission
                                            if (ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ||
                                                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED)
                                            {
                                                Log.i(TAG, "denied permission!");
                                                requestPermission();
                                            } else {
                                                // 检查是否有写入SD卡的授权
                                                Log.i(TAG, "granted permission!");
                                                turnOnCamera();
                                            }
                                        }
                                        chooseDialog.dismiss();
                                    }
                                })
                                .addViewOnclick(R.id.txtAlbum, new View.OnClickListener()  // 點選相簿
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        //開啟相簿相片集，須由startActivityForResult且帶入requestCode進行呼叫
                                        // 原因為點選相片後返回程式呼叫onActivityResult
                                        Intent intent = new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                                        // 注意此部分原本使用 ACTION_GET_CONTENT 後改使用 ACTION_OPEN_DOCUMENT
                                        startActivityForResult(intent, PHOTO);

                                        chooseDialog.dismiss();
                                    }
                                })
                                .build();
                                chooseDialog.show();

                break;
            default:
                break;
        }
    }

    /**
     * 開啟相機
     */
    private void turnOnCamera()
    {
        if (mCapturePhotoHelper == null)
        {
            mCapturePhotoHelper = new CapturePhotoHelper(getActivity(), FolderManager.getPhotoFolder());
        }
        mCapturePhotoHelper.capture();
    }

    /**
     * 申請權限
     */
    public void requestPermission()
    {
        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, RUNTIME_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "requestCode: " + requestCode + " resultCode: " + resultCode + " data: " + data);

        if (RESULT_OK != resultCode) {
            return;
        }

        switch (requestCode)
        {
            case CapturePhotoHelper.CAPTURE_PHOTO_REQUEST_CODE:

                if (mRestorePhotoFile != null)
                {
                    int requestWidth = (int) (RuleUtils.getScreenWidth(getActivity()) * RATIO);
                    int requestHeight = (int) (RuleUtils.getScreenHeight(getActivity()) * RATIO);
                    Bitmap bitmap = BitmapUtils.decodeBitmapFromFile(mRestorePhotoFile, requestWidth, requestHeight); // 按照屏幕宽高的3/4比例进行缩放显示
                    if (bitmap != null)
                    {
                        int degree = BitmapUtils.getBitmapDegree(mRestorePhotoFile.getAbsolutePath()); // 检查是否有被旋转，并进行纠正
                        if (degree != 0)
                        {
                            bitmap = BitmapUtils.rotateBitmapByDegree(bitmap, degree);
                        }
                        mImageViewPicture.setImageBitmap(bitmap);
                    }
                }

                // 先暫時留著處理沒有使用到的問題
                //File photoFile = mCapturePhotoHelper.getPhoto();
                /*else {
                    if (photoFile.exists())
                    {
                        photoFile .delete();
                    }
                }*/

                break;
            case PHOTO:

                if (mCapturePhotoHelper == null)
                { // 判斷是否有資料夾
                    mCapturePhotoHelper = new CapturePhotoHelper(getActivity(), FolderManager.getPhotoFolder());
                }

                Uri uri = data.getData();
                Log.d(TAG, "onActivityResult: " + uri);

                if (uri != null)
                {
                    spUtil.putString("PicturePath", String.valueOf(uri));
                    mImageViewPicture.setImageURI(uri);
                }
                break;
            default:
                break;
        }
    }

    // 檢查權限是否開啟，確認開啟後，打開相機
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RUNTIME_PERMISSION_REQUEST_CODE)
        {
            for (int index = 0; index < permissions.length; index++)
            {
                String permission = permissions[index];
                if (WRITE_EXTERNAL_STORAGE.equals(permission))
                {
                    if (grantResults[index] == PackageManager.PERMISSION_GRANTED)
                    {
                        Log.i(TAG, "onRequestPermissionsResult: permission is granted!");
                        _confirmCamera = true;
                    }
                }

                if(CAMERA.equals(permission))
                {
                    if (grantResults[index] == PackageManager.PERMISSION_GRANTED)
                    {
                        Log.i(TAG, "onRequestPermissionsResult: permission is granted!");
                        _confirmStorage = true;
                    }
                }

                if(_confirmCamera == true && _confirmStorage == true)
                {
                    turnOnCamera();
                } else {
                    Log.i(TAG, "onRequestPermissionsResult: permission is denied!");
                }
            }
        }
    }

    @Override
    public boolean onBackPressed()
    {
        return false;
    }

}
