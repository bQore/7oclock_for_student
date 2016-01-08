package net.sevenoclock.mobile.qna;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import net.sevenoclock.mobile.R;
import net.sevenoclock.mobile.settings.Values;

public class CameraEditor extends Activity {

    FrameLayout fl_cameraeditor_preview;
    Button btn_cameraeditor_capture;

    Values values;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cameraeditor);
        values = (Values) getApplicationContext();

        fl_cameraeditor_preview = (FrameLayout) findViewById(R.id.fl_cameraeditor_preview);
        btn_cameraeditor_capture = (Button) findViewById(R.id.btn_cameraeditor_capture);

        final CameraSurfaceView cameraView = new CameraSurfaceView(getApplicationContext());
        cameraView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        fl_cameraeditor_preview.addView(cameraView);

        fl_cameraeditor_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.setFocus();
            }
        });
        btn_cameraeditor_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.capture(new Camera.PictureCallback() {

                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                            //camera.startPreview();
                            values.capture_bitmap = BitmapFactory.decodeByteArray(data,0, data.length);
                            Intent intent = new Intent();
                            setResult(0, intent);
                            finish();
                        } catch (Exception e) {
                            Log.e("SampleCapture", "Failed to insert image.", e);
                        }
                    }
                });
            }
        });

    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

        private SurfaceHolder mHolder;

        private Camera camera = null;

        public CameraSurfaceView(Context context) {
            super(context);

            mHolder = getHolder();

            mHolder.addCallback(this);

            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            camera = Camera.open();

            try {
                camera.setPreviewDisplay(mHolder);
            } catch (Exception e) {
                Log.e("CameraSurfaceView", "Failed to set camera preview.", e);
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters= camera.getParameters();
            parameters.setPreviewSize(width, height);
            camera.setParameters(parameters);
            //camera.setDisplayOrientation(90);
            camera.startPreview();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }

        public void setFocus(){
            if (camera != null) {
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    public void onAutoFocus(boolean success, Camera camera) {

                    }
                });
            }
        }

        // 사진을 찍을때 호출되는 함수 (스냅샷)
        public boolean capture(final Camera.PictureCallback handler) {
            if (camera != null) {
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    public void onAutoFocus(boolean success, Camera camera) {
                        camera.takePicture(null, null, handler);
                    }
                });
                return true;
            } else {
                return false;
            }
        }

    }
}