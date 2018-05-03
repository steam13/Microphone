package nuance.com;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView.BufferType;

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.camera.CameraManager;

import java.util.regex.Pattern;
import nuance.com.db.NuanceDatabaseAdapter;
import nuance.com.db.NuanceQueryConstants;
import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddProfileScreen extends NuanceActivity implements OnClickListener, NuanceQueryConstants {
    private static String ADDRESSES = "addresses";
    private static String NAME = "name";
    private static String PORT = "port";
    private ArrayAdapter<CharSequence> adapter;
    private Button btnCancel;
    private Button btnDone;
    private Button btnQR;
    private EditText edtComputerName;
    private EditText edtPort;
    private EditText edtUserName;
    private NuanceDatabaseAdapter nuanceAdapter;
    private Spinner spinner;

    private class SpinnerItemSelectedListener implements OnItemSelectedListener {
        private SpinnerItemSelectedListener() {
        }

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            AddProfileScreen.this.edtComputerName.setText(parent.getItemAtPosition(pos).toString(), BufferType.EDITABLE);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_entry);
        getWindow().setTitle(getString(R.string.Manual_Entry));
        this.edtUserName = (EditText) findViewById(R.id.EditText_UserName);
        this.edtUserName.setFocusable(true);
        this.edtComputerName = (EditText) findViewById(R.id.EditText_ComputerName);
        this.edtPort = (EditText) findViewById(R.id.EditText_Port);
        this.spinner = (Spinner) findViewById(R.id.Spinner);
        this.spinner.setVisibility(8);
        this.adapter = new ArrayAdapter(this, 17367048);
        this.adapter.setDropDownViewResource(17367050);
        this.spinner.setAdapter(this.adapter);
        this.spinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
        this.btnQR = (Button) findViewById(R.id.QR_Button);
        this.btnQR.setOnClickListener(this);
        this.btnDone = (Button) findViewById(R.id.buttonDone);
        this.btnCancel = (Button) findViewById(R.id.buttonCancel);
        this.btnDone.setOnClickListener(this);
        this.btnCancel.setOnClickListener(this);
        this.nuanceAdapter = new NuanceDatabaseAdapter(this);
        this.nuanceAdapter.open();
        Intent editIntent = getIntent();
        if (editIntent.hasExtra(NuanceKeys.PROFILE_DATA)) {
            ProfileData profileData = (ProfileData) editIntent.getExtras().getParcelable(NuanceKeys.PROFILE_DATA);
            this.edtUserName.setText(profileData.getProfileName());
            this.edtComputerName.setText(profileData.getDnsNames());
            this.edtPort.setText(new StringBuilder(String.valueOf(profileData.getPort())).toString());
        }
        if (editIntent.hasExtra(NuanceKeys.MICEDITPROFILE)) {
            ProfileData profileData = (ProfileData) editIntent.getExtras().getParcelable(NuanceKeys.MICEDITPROFILE);
            this.edtUserName.setText(profileData.getProfileName());
            this.edtComputerName.setText(profileData.getDnsNames());
            this.edtPort.setText(new StringBuilder(String.valueOf(profileData.getPort())).toString());
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.nuanceAdapter.close();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0 && resultCode == -1) {
            try {
                JSONObject json = new JSONObject(intent.getStringExtra(Intents.Scan.RESULT));
                String name = (String) json.get(NAME);
                if (name != null) {
                    this.edtUserName.setText(name, BufferType.EDITABLE);
                }
                String port = (String) json.get(PORT);
                if (port != null) {
                    this.edtPort.setText(port, BufferType.EDITABLE);
                }
                this.adapter.clear();
                JSONArray addresses = (JSONArray) json.get(ADDRESSES);
                if (addresses != null && addresses.length() > 0) {
                    for (int i = 0; i < addresses.length(); i++) {
                        this.adapter.add((String) addresses.get(i));
                    }
                }
                if (this.adapter.getCount() == 1) {
                    this.edtComputerName.setText((CharSequence) this.adapter.getItem(0), BufferType.EDITABLE);
                } else if (this.adapter.getCount() > 1) {
                    this.spinner.setVisibility(0);
                }
                this.btnDone.setEnabled(true);
            } catch (JSONException e) {
                MessageBox.showOK(this, getString(R.string.bad_barcode_Message), getString(R.string.Problem_reading_bar_code));
            } catch (ClassCastException e2) {
                MessageBox.showOK(this, getString(R.string.bad_barcode_Message), getString(R.string.Problem_reading_bar_code));
            }
        }
    }

    public void onClick(View v) {
        if (v == this.btnQR) {
            Intent intent = new Intent("SCAN");
            intent.putExtra(Intents.Scan.MODE, Intents.Scan.QR_CODE_MODE);
            startActivityForResult(intent, 0);
        }
        if (v == this.btnCancel) {
            setResult(0, getIntent());
            finish();
        }
        if (v == this.btnDone) {
            String profileName = this.edtUserName.getText().toString();
            String computerName = this.edtComputerName.getText().toString();
            String port = this.edtPort.getText().toString();
            if (port == null || port.trim().length() == 0) {
                port = "51001";
            }
            Intent intent2 = getIntent();
            if (profileName == null || profileName.trim().length() <= 0 || computerName == null || computerName.trim().length() <= 0 || port == null || port.trim().length() <= 0) {
                MessageBox.showOK(this, getString(R.string.mandatory_inputfield_message), getString(R.string.please_verify_input));
            } else if (!validateIpAddress(computerName)) {
                MessageBox.showOK(this, "\"" + computerName + "\" " + getString(R.string.invalid_ipaddress_message), getString(R.string.please_verify_input));
            } else if (validatePortNumber(port)) {
                ProfileData profileData;
                if (intent2.hasExtra(NuanceKeys.PROFILE_DATA)) {
                    Bundle b1 = intent2.getExtras();
                    profileData = (ProfileData) b1.getParcelable(NuanceKeys.PROFILE_DATA);
                    profileData.setProfileName(profileName);
                    profileData.setDnsNames(computerName);
                    profileData.setPort(Integer.parseInt(port));
                    profileData.setCb(true);
                    b1.putParcelable(NuanceKeys.PROFILE_DATA, profileData);
                    intent2.putExtras(b1);
                } else {
                    if (intent2.hasExtra(NuanceKeys.MICADDPROFILE)) {
                        this.nuanceAdapter.insertProfile(profileName, computerName, "ipAddress", port, true);
                    } else {
                        if (intent2.hasExtra(NuanceKeys.MICEDITPROFILE)) {
                            profileData = (ProfileData) intent2.getExtras().getParcelable(NuanceKeys.MICEDITPROFILE);
                            profileData.setProfileName(profileName);
                            profileData.setDnsNames(computerName);
                            profileData.setPort(Integer.parseInt(port));
                            profileData.setCb(true);
                            updateProfile(profileData);
                        } else {
                            String str = profileName;
                            String str2 = computerName;
                            profileData = new ProfileData(0, str, str2, "ipAddress", Integer.parseInt(port), true);
                            Bundle extras = new Bundle();
                            extras.putParcelable(NuanceKeys.PROFILE_DATA, profileData);
                            intent2.putExtras(extras);
                        }
                    }
                }
                setResult(-1, intent2);
                finish();
            } else {
                MessageBox.showOK(this, "\"" + port + "\" " + getString(R.string.invalid_port_message), getString(R.string.please_verify_input));
            }
        }
    }

    public void updateProfile(ProfileData profileData) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NuanceQueryConstants.PROFILE_NAME, profileData.getProfileName());
        initialValues.put(NuanceQueryConstants.DNS_NAME, profileData.getDnsNames());
        initialValues.put(NuanceQueryConstants.IP_ADDRESS, profileData.getAddresses());
        initialValues.put(PORT, Integer.valueOf(profileData.getPort()));
        initialValues.put(NuanceQueryConstants.SELECTION, Boolean.valueOf(profileData.isCb()));
        String whereClause = "profile_id = ?";
        this.nuanceAdapter.updateRecordInDB(NuanceQueryConstants.DATABASE_TABLE_PROFILE, initialValues, "profile_id = ?", new String[]{new StringBuilder(String.valueOf(profileData.getProfileId())).toString()});
    }

    public boolean validateIpAddress(String ipAddress) {
        return InetAddressUtils.isIPv4Address(ipAddress);
    }

    public boolean validatePortNumber(String portNumber) {
        String PORT_NUMBER_PATTERN = "(6553[0-5]|655[0-2]\\d|65[0-4]\\d{2}|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3})";
        return Pattern.compile("(6553[0-5]|655[0-2]\\d|65[0-4]\\d{2}|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3})").matcher(portNumber).matches();
    }
}
