package com.example.weatherforecast.Activity;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.weatherforecast.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends Fragment {

    //二维码容器声明
    ImageView imgQRCode;


    public RecommendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //创建二维码
        imgQRCode=getActivity().findViewById(R.id.imgQRCode);
        Bitmap bitmap = createQRCodeBitmap("www.hznu.edu.cn",800,800,"UTF-8","H","2");
        imgQRCode.setImageBitmap(bitmap);
    }

    public static Bitmap createQRCodeBitmap(String content, int width, int height, String character_set,
                                            String error_correction_level, String margin){

        int color_black=Color.BLACK;
        int color_white=Color.WHITE;

        if(TextUtils.isEmpty(content)||width<0||height<0){
            return null;
        }


        try{
            Hashtable<EncodeHintType,String> hints=new Hashtable<>();
            //字符转码格式
            if(!TextUtils.isEmpty(character_set)){
                hints.put(EncodeHintType.CHARACTER_SET,character_set);
            }
            //容错率设置
            if(!TextUtils.isEmpty(error_correction_level)){
                hints.put(EncodeHintType.ERROR_CORRECTION,error_correction_level);
            }
            //边距设置
            if(!TextUtils.isEmpty(margin)){
                hints.put(EncodeHintType.MARGIN,margin);
            }

            //生成位矩阵
            BitMatrix bitMatrix=new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE,width,height,hints);
            //创建像素数组
            int[] pixels = new int[width * height];
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    if(bitMatrix.get(x, y)){
                        pixels[y * width + x] = color_black; // 黑色色块像素设置
                    } else {
                        pixels[y * width + x] = color_white; // 白色色块像素设置
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
