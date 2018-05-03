package nuance.com;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

public class InformationScreen extends NuanceActivity {
    private static final String BASE_URL = "file:///android_asset/html";
    private static final String DEFAULT_COUNTRY = "US";
    private static final String DEFAULT_LANGUAGE = "";
    public static final String DEFAULT_PAGE = "dragon_remote_mic.htm";
    private static final Collection<String> TRANSLATED_HELP_ASSET_LANGUAGES = Arrays.asList(new String[]{"de", "it", "fr", "es", "nl", "pt-rBR", "ja"});
    private NuanceWebView infoWebView;

    class C00071 implements Factory {
        C00071() {
        }

        public View onCreateView(String name, Context context, AttributeSet attrs) {
            if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
                try {
                    final View view = InformationScreen.this.getLayoutInflater().createView(name, null, attrs);
                    new Handler().post(new Runnable() {
                        public void run() {
                            view.setBackgroundResource(R.color.Black);
                            ((TextView) view).setTextColor(-1);
                        }
                    });
                    return view;
                } catch (InflateException e) {
                } catch (ClassNotFoundException e2) {
                }
            }
            return null;
        }
    }

    private class InfoWebviewClient extends WebViewClient {
        private InfoWebviewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("nuance.com") || url.contains("www.nuance")) {
                InformationScreen.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                view.stopLoading();
                return false;
            }
            view.loadUrl(url);
            return true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.aboutdragon);
        this.infoWebView = (NuanceWebView) findViewById(R.id.infowebView);
        this.infoWebView.getSettings().setBuiltInZoomControls(true);
        this.infoWebView.loadUrl(new StringBuilder(BASE_URL).append(getTranslatedAssetLanguage()).append('/').append(DEFAULT_PAGE).toString());
        this.infoWebView.setWebViewClient(new InfoWebviewClient());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.information_menu, menu);
        getLayoutInflater().setFactory(new C00071());
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu_item:
                startActivityForResult(new Intent(this, SettingScreen.class), 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }

    private static String getLanguage() {
        String language;
        Locale locale = Locale.getDefault();
        String country = locale == null ? DEFAULT_COUNTRY : locale.getCountry();
        if (locale == null) {
            language = DEFAULT_LANGUAGE;
        } else {
            language = locale.getLanguage();
        }
        if (Locale.SIMPLIFIED_CHINESE.getLanguage().equals(language)) {
            return new StringBuilder(String.valueOf(language)).append("-r").append(country).toString();
        }
        if (language.equals("pt")) {
            return new StringBuilder(String.valueOf(language)).append("-r").append(country).toString();
        }
        return language;
    }

    private static String getTranslatedAssetLanguage() {
        String language = getLanguage();
        if (TRANSLATED_HELP_ASSET_LANGUAGES.contains(language)) {
            return "-" + language;
        }
        return DEFAULT_LANGUAGE;
    }
}
