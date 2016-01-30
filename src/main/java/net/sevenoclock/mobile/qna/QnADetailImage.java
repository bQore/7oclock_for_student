package net.sevenoclock.mobile.qna;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import net.sevenoclock.mobile.R;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Bear on 2016-01-12.
 */
public class QnADetailImage extends Activity {

    private ImageView img;
    private PhotoViewAttacher atthacher;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qna_detail_image);

        byte[] img_bytes = getIntent().getByteArrayExtra("img");
        Bitmap bitmap = BitmapFactory.decodeByteArray(img_bytes, 0, img_bytes.length);

        img = (ImageView)findViewById(R.id.largeImageView);
        img.setImageBitmap(bitmap);

        atthacher = new PhotoViewAttacher(img);
        atthacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }
}
