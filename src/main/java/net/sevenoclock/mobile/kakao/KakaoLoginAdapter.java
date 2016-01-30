package net.sevenoclock.mobile.kakao;

import android.app.Activity;
import android.content.Context;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.KakaoAdapter;
import net.sevenoclock.mobile.home.LandingActivity;

/**
 * Created by Bear on 2016-01-12.
 */
public class KakaoLoginAdapter extends KakaoAdapter {

    @Override
    public IApplicationConfig getApplicationConfig() {
        return new IApplicationConfig() {
            @Override
            public Activity getTopActivity() {
                return new LandingActivity();
            }

            @Override
            public Context getApplicationContext() {
                return LandingActivity.context;
            }
        };
    }
}
