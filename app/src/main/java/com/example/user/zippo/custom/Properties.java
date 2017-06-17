package com.example.user.zippo.custom;

/**
 * 暫時棄用程式碼
 */

public class Properties
{
    /*ArrayList data = new ArrayList<DataNote>();
        for (int i = 0; i < DataNoteInformation.id.length; i++)
        {
            data.add(
                    new DataNote
                            (
                                    DataNoteInformation.id[i],
                                    DataNoteInformation.textArray[i],
                                    DataNoteInformation.dateArray[i]
                            ));
        }*/


    /**
     *  接收回傳資料
     * @param requestCode
     * @param resultCode
     * @param data
     */
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            // 藉由requestCode判斷是否為開啟相機或開啟相簿而呼叫的，且data不為null
            if (requestCode == PHOTO && data != null)
            {
                // 取得照片路徑 uri
                Uri uri = data.getData();
                ContentResolver cr = getActivity().getContentResolver();

                try {
                    // 讀取照片，型態為Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                    // 判斷照片為橫向或者為直向，並進入ScalePic判斷圖片是否要進行縮放
                    if (bitmap.getWidth() > bitmap.getHeight())
                    {
                        ScalePic(bitmap, mPhone.heightPixels);
                    } else {
                        ScalePic(bitmap, mPhone.widthPixels);
                    }
                } catch (FileNotFoundException e) {
                }
            } else {
                Bitmap bmp = BitmapFactory.decodeFile(file.getPath());
                mImageViewPicture.setImageBitmap(bmp);
                spUtil.putString("PicturePath", file.getPath());
            }
        }
    }*/

    /**
     * 判斷圖片是否需要調整，進行縮放
     * @param bitmap 顯示圖片
     * @param phone
     */
    /*private void ScalePic(Bitmap bitmap, int phone)
    {
        // 縮放比例預設為1
        float mScale = 1 ;

        // 如果圖片寬度大於手機寬度則進行縮放，否則直接將圖片放入 ImageView 內
        if( bitmap.getWidth() > phone )
        {
            //判斷縮放比例
            mScale = (float)phone / (float)bitmap.getWidth();

            Matrix mMat = new Matrix() ;
            mMat.setScale(mScale, mScale);

            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,
                    0,
                    0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    mMat,
                    false);
            mImageViewPicture.setImageBitmap(mScaleBitmap);
        } else {
            mImageViewPicture.setImageBitmap(bitmap); // 直接放入
        }
    }*/
}
