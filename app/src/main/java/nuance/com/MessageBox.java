package nuance.com;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MessageBox {



    static void showOK(Context context, String string) {
        Builder builder = new Builder(context);
        builder.setMessage(string).setCancelable(true).setNeutralButton(R.string.OK, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    static AlertDialog showOKGetInstance(Context context, String string) {
        Builder builder = new Builder(context);
        builder.setMessage(string).setCancelable(true).setNeutralButton(R.string.OK, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static void showOK(Activity activity, String string, String title) {
        TextView titleView = (TextView) activity.getLayoutInflater().inflate(R.layout.dialog_title, (ViewGroup) activity.findViewById(R.id.dialog_title));
        titleView.setText(title);
        Builder builder = new Builder(activity);
        builder.setCustomTitle(titleView).setMessage(string).setCancelable(true).setNeutralButton(R.string.OK, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    static void showOK(Context context, int string_id) {
        showOK(context, context.getResources().getString(string_id));
    }
}
