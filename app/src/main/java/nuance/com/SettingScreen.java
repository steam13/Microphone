package nuance.com;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import nuance.com.db.NuanceDatabaseAdapter;
import nuance.com.db.NuanceQueryConstants;

public class SettingScreen extends NuanceActivity implements NuanceQueryConstants {
    private static final int EDIT_CODE = 2;
    private static final int REQUEST_CODE = 1;
    private final OnItemClickListener itemClickListener = new C00191();
    private NuanceDatabaseAdapter nuanceDatabaseAdapter;
    private ProfileAdapter profileAdapter;
    private List<ProfileData> profileList;
    private ListView profileListView;

    class C00191 implements OnItemClickListener {
        C00191() {
        }

        public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
            ProfileData pfData = (ProfileData) SettingScreen.this.profileList.get(position);
            pfData.setCb(true);
            SettingScreen.this.profileAdapter.updateDBprofile(pfData);
            SettingScreen.this.profileAdapter.updateProfileSelection(pfData);
            SettingScreen.this.finish();
        }
    }

    class C00202 implements OnClickListener {
        C00202() {
        }

        public void onClick(View v) {
            Intent intent = new Intent();
            SettingScreen.this.startActivityForResult(new Intent(SettingScreen.this, AddProfileScreen.class), 1);
        }
    }

    class C00223 implements Factory {
        C00223() {
        }

        public View onCreateView(String name, Context context, AttributeSet attrs) {
            if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
                try {
                    final View view = SettingScreen.this.getLayoutInflater().createView(name, null, attrs);
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

    static class Holder {
        ImageView chevronImageView;
        TextView computerNameTextView;
        TextView profilNameTextView;

        Holder() {
        }
    }

    public class ProfileAdapter extends BaseAdapter implements NuanceQueryConstants {
        Context context;
        private final LayoutInflater inflater;
        private final List<ProfileData> profileList;

        public ProfileAdapter(Context context, List<ProfileData> list) {
            this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
            this.context = context;
            this.profileList = list;
        }

        public int getCount() {
            return this.profileList.size();
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder = new Holder();
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.profile_list_item, parent, false);
                holder.profilNameTextView = (TextView) convertView.findViewById(R.id.profiletextView);
                holder.computerNameTextView = (TextView) convertView.findViewById(R.id.computertextView);
                holder.chevronImageView = (ImageView) convertView.findViewById(R.id.chevronimageView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
            checkBox.setFocusable(false);
            checkBox.setFocusableInTouchMode(false);
            checkBox.setChecked(((ProfileData) this.profileList.get(position)).isCb());
            checkBox.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    for (ProfileData pdata : ProfileAdapter.this.profileList) {
                        pdata.setCb(false);
                        ProfileAdapter.this.updateProfileSelection(pdata);
                    }
                    ProfileAdapter.this.notifyDataSetChanged();
                    ((ProfileData) ProfileAdapter.this.profileList.get(position)).cbToggle();
                    ProfileAdapter.this.updateProfileSelection((ProfileData) ProfileAdapter.this.profileList.get(position));
                }
            });
            checkBox.setVisibility(0);
            holder.profilNameTextView.setText(((ProfileData) this.profileList.get(position)).getProfileName());
            holder.computerNameTextView.setText(new StringBuilder(String.valueOf(((ProfileData) this.profileList.get(position)).getDnsNames())).append(":").append(((ProfileData) this.profileList.get(position)).getPort()).toString());
            holder.computerNameTextView.setVisibility(0);
            holder.chevronImageView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent editProfileIntent = new Intent();
                    Bundle b1 = new Bundle();
                    b1.putParcelable(NuanceKeys.PROFILE_DATA, (Parcelable) ProfileAdapter.this.profileList.get(position));
                    editProfileIntent.putExtras(b1);
                    editProfileIntent.setClassName("nuance.com", "nuance.com.AddProfileScreen");
                    SettingScreen.this.startActivityForResult(editProfileIntent, 2);
                }
            });
            return convertView;
        }

        public void updateProfileSelection(ProfileData pfData) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(NuanceQueryConstants.PROFILE_NAME, pfData.getProfileName());
            initialValues.put(NuanceQueryConstants.DNS_NAME, pfData.getDnsNames());
            initialValues.put(NuanceQueryConstants.IP_ADDRESS, pfData.getAddresses());
            initialValues.put("port", Integer.valueOf(pfData.getPort()));
            initialValues.put(NuanceQueryConstants.SELECTION, Boolean.valueOf(pfData.isCb()));
            String whereClause = "profile_id = ?";
            SettingScreen.this.nuanceDatabaseAdapter.updateRecordInDB(NuanceQueryConstants.DATABASE_TABLE_PROFILE, initialValues, "profile_id = ?", new String[]{new StringBuilder(String.valueOf(pfData.getProfileId())).toString()});
        }

        public void updateDBprofile(ProfileData pfData) {
            for (ProfileData pdata : this.profileList) {
                pdata.setCb(false);
                updateProfileSelection(pdata);
            }
            pfData.setCb(true);
            updateProfileSelection(pfData);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        this.profileListView = (ListView) findViewById(R.id.profilelistView);
        this.profileList = new ArrayList();
        this.profileAdapter = new ProfileAdapter(this, this.profileList);
        this.profileListView.setAdapter(this.profileAdapter);
        this.profileListView.setOnItemClickListener(this.itemClickListener);
        this.nuanceDatabaseAdapter = new NuanceDatabaseAdapter(this);
        this.nuanceDatabaseAdapter.open();
        getProfileListFromDB();
        ((LinearLayout) findViewById(R.id.manualEntry)).setOnClickListener(new C00202());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == -1) {
            ProfileData profileData = (ProfileData) data.getExtras().getParcelable(NuanceKeys.PROFILE_DATA);
            String profileName = profileData.getProfileName();
            String dnsNames = profileData.getDnsNames();
            String ipAddress = profileData.getAddresses();
            int port = profileData.getPort();
            this.nuanceDatabaseAdapter.insertProfile(profileName, dnsNames, ipAddress, new StringBuilder(String.valueOf(port)).toString(), profileData.isCb());
            this.profileAdapter.updateDBprofile(profileData);
            this.profileList.clear();
            getProfileListFromDB();
            finish();
        }
        if (requestCode == 2 && resultCode == -1) {
            this.profileAdapter.updateDBprofile((ProfileData) data.getExtras().getParcelable(NuanceKeys.PROFILE_DATA));
            this.profileList.clear();
            getProfileListFromDB();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        getLayoutInflater().setFactory(new C00223());
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editProfile_menu_item:
                ProfileData pfd = getSelectedProfileDatat();
                if (pfd == null) {
                    return true;
                }
                Intent editProfileIntent = new Intent(this, AddProfileScreen.class);
                Bundle b1 = new Bundle();
                b1.putParcelable(NuanceKeys.PROFILE_DATA, pfd);
                editProfileIntent.putExtras(b1);
                startActivityForResult(editProfileIntent, 2);
                return true;
            case R.id.deleteProfile_menu_item:
                ProfileData pfd1 = getSelectedProfileDatat();
                if (pfd1 == null) {
                    return true;
                }
                deleteProfile(pfd1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void editProfile(ProfileData profileData) {
        Intent editProfileIntent = new Intent(this, AddProfileScreen.class);
        Bundle b1 = new Bundle();
        b1.putParcelable(NuanceKeys.PROFILE_DATA, profileData);
        editProfileIntent.putExtras(b1);
        startActivityForResult(editProfileIntent, 2);
    }

    public final void getProfileListFromDB() {
        Cursor c = this.nuanceDatabaseAdapter.selectRecordsFromDB(NuanceQueryConstants.DATABASE_TABLE_PROFILE, new String[]{NuanceQueryConstants.PROFILE_ID, NuanceQueryConstants.PROFILE_NAME, NuanceQueryConstants.DNS_NAME, NuanceQueryConstants.IP_ADDRESS, "port", NuanceQueryConstants.SELECTION}, null, null, null, null, null);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                boolean check;
                int profileId = Integer.parseInt(c.getString(c.getColumnIndex(NuanceQueryConstants.PROFILE_ID)));
                String profileName = c.getString(c.getColumnIndex(NuanceQueryConstants.PROFILE_NAME));
                String dnsNames = c.getString(c.getColumnIndex(NuanceQueryConstants.DNS_NAME));
                String addresses = c.getString(c.getColumnIndex(NuanceQueryConstants.IP_ADDRESS));
                int port = Integer.parseInt(c.getString(c.getColumnIndex("port")));
                if (c.getInt(c.getColumnIndex(NuanceQueryConstants.SELECTION)) > 0) {
                    check = true;
                } else {
                    check = false;
                }
                this.profileList.add(new ProfileData(profileId, profileName, dnsNames, addresses, port, check));
            }
            this.profileAdapter.notifyDataSetChanged();
            c.close();
            return;
        }
        this.profileAdapter.notifyDataSetChanged();
        c.close();
    }

    public ProfileData getSelectedProfileDatat() {
        ProfileData pfd = null;
        for (ProfileData pfData : this.profileList) {
            if (pfData.isCb()) {
                pfd = pfData;
            }
        }
        return pfd;
    }

    public ProfileData nextSelectedProfileData(ProfileData selectedProfileData) {
        if (this.profileList.size() == 1) {
            return null;
        }
        int index = this.profileList.indexOf(selectedProfileData);
        if (index > 0) {
            return (ProfileData) this.profileList.get(index - 1);
        }
        return (ProfileData) this.profileList.get(1);
    }

    public void deleteProfile(ProfileData profileData) {
        String whereClause = "profile_id = ? ";
        this.nuanceDatabaseAdapter.deleteRecordsFromDB(NuanceQueryConstants.DATABASE_TABLE_PROFILE, "profile_id = ? ", new String[]{new StringBuilder(String.valueOf(profileData.getProfileId())).toString()});
        ProfileData profile = nextSelectedProfileData(profileData);
        if (profile != null) {
            profile.setCb(true);
            this.profileAdapter.updateProfileSelection(profile);
        }
        this.profileList.clear();
        getProfileListFromDB();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.nuanceDatabaseAdapter.close();
    }
}
