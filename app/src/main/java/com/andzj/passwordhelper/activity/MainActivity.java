package com.andzj.passwordhelper.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andzj.passwordhelper.activity.alteremailaddress.ConfirmOldEmailAddressActivity;
import com.andzj.passwordhelper.activity.alterpasswordlock.ConfirmOldPasswordLockActivity;
import com.andzj.passwordhelper.encrypt.RSAKey;
import com.andzj.passwordhelper.R;
import com.andzj.passwordhelper.adapter.AccountPasswordItemAdapter;
import com.andzj.passwordhelper.bean.AccountPasswordInfo;
import com.andzj.passwordhelper.bean.UserInfo;
import com.andzj.passwordhelper.db.PasswordDataBaseHelper;
import com.andzj.passwordhelper.encrypt.MyEncryption;
import com.andzj.passwordhelper.log.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * 记得做输入框的长度限制
 */
public class MainActivity extends MyAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String TAG = "MainActivity";

    public static boolean isConfirmed = false;
    private static UserInfo userInfo = new UserInfo();

    public static UserInfo getUserInfo()
    {
        return userInfo;
    }

    private DrawerLayout drawer = null;
    private List<AccountPasswordInfo> infoShownList = new ArrayList<>(64);
    public static List<AccountPasswordInfo> allInfoList = new ArrayList<>(64);
    private ListView accountPasswordInfoListView = null;
    private AccountPasswordItemAdapter accountPasswordItemAdapter = null;

    private static final int MODE_VIEW = 11;
    private static final int MODE_SEARCH = 12;
    private int currentMode = MODE_VIEW;

    private RelativeLayout searchLayout;
    private EditText searchEdit;
    private InputMethodManager imm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                MyLog.d(TAG,"fab",true);
                accountPasswordInfoListView.smoothScrollToPosition(0);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        accountPasswordInfoListView = (ListView) findViewById(R.id.account_password_info_list_view);
        accountPasswordItemAdapter = new AccountPasswordItemAdapter(MainActivity.this, infoShownList);
        accountPasswordInfoListView.setAdapter(accountPasswordItemAdapter);
        accountPasswordInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountPasswordDetailInfoActivity.actionStart(MainActivity.this, infoShownList.get(position));
            }
        });

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        searchLayout = (RelativeLayout) findViewById(R.id.search_layout);
        searchEdit = (EditText) findViewById(R.id.search_edit);
        searchEdit.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchEdit.setSingleLine();
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    if (!"".equals(v.getText().toString()))
                    {
                        //MyLog.d("SearchBookActivity","Enter 事件",false);
                        doSearchOperate(v.getText().toString());
                        if (imm != null)
                        {
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });
        new LoadInfoTask().execute();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //MyLog.d(TAG,"close_drawer",true);
            drawer.closeDrawer(GravityCompat.START);
        } else if (currentMode == MODE_SEARCH){
            setViewMode();
        } else{
            //MyLog.d(TAG,"exit",true);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (currentMode)
        {
            case MODE_VIEW:
                menu.findItem(R.id.close_btn).setVisible(false);
                menu.findItem(R.id.refresh_btn).setVisible(true);
                break;
            case MODE_SEARCH:
                menu.findItem(R.id.refresh_btn).setVisible(false);
                menu.findItem(R.id.close_btn).setVisible(true);
                break;
            default:
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId())
        {
            case R.id.refresh_btn:
            {
                new LoadInfoTask().execute();
                break;
            }
            case R.id.search_btn:
            {
                switch (currentMode)
                {
                    case MODE_VIEW:
                        setSearchMode();
                        break;
                    case MODE_SEARCH:
                        doSearchOperate(searchEdit.getText().toString());
                        break;
                    default:
                        break;
                }
                break;
            }
            case R.id.close_btn:
            {
                setViewMode();

                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId())
        {
            case R.id.add_account_password_menu:
                AddAccountPasswordActivity.actionStart(MainActivity.this);
                break;
            case R.id.change_email_address:
                ConfirmOldEmailAddressActivity.actionStart(MainActivity.this);
                break;
            case R.id.change_login_password:
                ConfirmOldPasswordLockActivity.actionStart(MainActivity.this);
                break;
            case R.id.import_info_menu:
                ImportBackupFileActivity.actionStart(MainActivity.this);
                break;
            case R.id.export_info_menu:
                ExportBackupFileActivity.actionStart(MainActivity.this);
                break;
            case R.id.test_menu_item:
                MyLog.d(TAG,"test");
                //TestActivity.actionStart(MainActivity.this);
                String s=null;
                s.getBytes();
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case AddAccountPasswordActivity.REQUEST_CODE:
            {
                if (resultCode == RESULT_OK)
                {
                    AccountPasswordInfo addAccountPasswordInfo = data.getParcelableExtra("info");
                    if (addAccountPasswordInfo != null)
                    {
                        addListData(addAccountPasswordInfo);
                    }
                }
                break;
            }
            case AccountPasswordDetailInfoActivity.REQUEST_CODE:
            {
                if (resultCode == RESULT_OK)
                {
                    AccountPasswordInfo addAccountPasswordInfo = data.getParcelableExtra("info");

                    if (addAccountPasswordInfo != null)
                    {
                        if (data.getBooleanExtra("deleted",false))
                        {
                            deleteListData(addAccountPasswordInfo);
                        }
                        else
                        {
                            updateListData(addAccountPasswordInfo);
                        }
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    private void doSearchOperate(String words)
    {
        if (!isEmpty(words))
        {
            new SearchInfoTask().execute(words);
        }
    }

    private void setViewMode()
    {
        currentMode = MODE_VIEW;
        invalidateOptionsMenu();
        searchLayout.setVisibility(View.GONE);
        if (imm != null)
        {
            View view = getWindow().getCurrentFocus();
            if (view != null)
            {
                imm.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        infoShownList.clear();
        refreshInfoShownList(allInfoList);
    }

    private void setSearchMode()
    {
        currentMode = MODE_SEARCH;
        invalidateOptionsMenu();
        searchLayout.setVisibility(View.VISIBLE);
        searchEdit.setText("");
        if (imm != null)
        {
            searchEdit.requestFocus();
            imm.showSoftInput(searchEdit,0);
            searchEdit.setSelection(searchEdit.getText().length());
        }
    }

    private void refreshInfoShownList(List<AccountPasswordInfo> accountPasswordInfos)
    {
        infoShownList.addAll(accountPasswordInfos);
        accountPasswordItemAdapter.notifyDataSetChanged();
    }


    private void addListData(AccountPasswordInfo accountPasswordInfo)
    {
        allInfoList.add(0,accountPasswordInfo);
        if (currentMode == MODE_VIEW)
        {
            infoShownList.add(0,accountPasswordInfo);
        }
        accountPasswordItemAdapter.notifyDataSetChanged();
    }

    private void updateListData(AccountPasswordInfo accountPasswordInfo)
    {
        for (AccountPasswordInfo info: allInfoList)
        {
            if (info.getId().intValue() == accountPasswordInfo.getId().intValue())
            {
                info.update(accountPasswordInfo);
                break;
            }
        }
//        for (AccountPasswordInfo info:infoShownList)
//        {
//            if (info.getId().intValue() == accountPasswordInfo.getId().intValue())
//            {
//                info.update(accountPasswordInfo);
//                break;
//            }
//        }
        accountPasswordItemAdapter.notifyDataSetChanged();
    }

    private void deleteListData(AccountPasswordInfo accountPasswordInfo)
    {
        for (AccountPasswordInfo info: allInfoList)
        {
            if (info.getId().intValue() == accountPasswordInfo.getId().intValue())
            {
                allInfoList.remove(info);
                break;
            }
        }
        for (AccountPasswordInfo info: infoShownList)
        {
            if (info.getId().intValue() == accountPasswordInfo.getId().intValue())
            {
                infoShownList.remove(info);
                break;
            }
        }
        accountPasswordItemAdapter.notifyDataSetChanged();
    }

    private boolean isEmpty(String s)
    {
        return s == null || "".equals(s);
    }

    private class SearchInfoTask extends AsyncTask<String,Void,Boolean>
    {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        List<AccountPasswordInfo> infos = new ArrayList<>(64);
        List<String> searchWords = null ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            infoShownList.clear();
            progressDialog.setTitle("正在查找");
            progressDialog.setMessage("请稍候...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            searchWords = getSearchWords(params[0]);
            int size = allInfoList.size();
            for (int i = 0; i < size ; ++i)
            {
                if (isInfoContainWords(allInfoList.get(i)))
                {
                    infos.add(allInfoList.get(i));
                }
            }
            return infos != null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean)
            {
                refreshInfoShownList(infos);
            }
            else if (infos.isEmpty())
            {
                Toast.makeText(MainActivity.this,"对不起，没有找到",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(MainActivity.this,"出现了问题，无法加载数据",Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }

        private boolean isInfoContainWords(AccountPasswordInfo info)
        {
            if (info != null)
            {
                if (isStringContainWords(info.getAccountCategory()) || isStringContainWords(info.getBindCooperationCategory()))
                {
                    return true;
                }
            }
            return false;
        }

        private boolean isStringContainWords(String str)
        {
            if (!isEmpty(str))
            {
                System.out.println("Search in " + str);
                for (int i = 0; i< searchWords.size() ; ++i)
                {
                    if (str.toLowerCase().contains(searchWords.get(i)))
                    {
                        return true;
                    }
                }
            }
            return false;
        }

        private List<String> getSearchWords(String searchWord)
        {
            StringBuffer stringBuffer = new StringBuffer(searchWord.trim());
            if (stringBuffer.length() == 0)
            {
                return null;
            }
            List<String> words = new ArrayList<>();
            int i = 0;
            while ( ( i = stringBuffer.indexOf(" ")) >= 0)
            {
                if (i != 0)
                {
                    words.add(stringBuffer.substring(0,i).toLowerCase());
                }
                stringBuffer.delete(0,i+1);
            }
            words.add(stringBuffer.toString().toLowerCase());
            if (words.size() > 0)
            {
                return words;
            }
            return null;
        }

    }

    private class LoadInfoTask extends AsyncTask<Void,Integer,Boolean>
    {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            infoShownList.clear();
            allInfoList.clear();
            progressDialog.setTitle("正在加载数据,请稍候...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<AccountPasswordInfo> encryptedInfoList;
            encryptedInfoList = PasswordDataBaseHelper.readAllEncryptedInfo();
            int size = encryptedInfoList.size();
            for (int i = 0;i < size; ++i)
            {
                publishProgress(size,i);
                allInfoList.add(MyEncryption.decryptAccountPasswordInfo(encryptedInfoList.get(i), RSAKey.getSavePrivateKey()));
            }
            return allInfoList != null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setMax(values[0]);
            progressDialog.setProgress(values[1]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean)
            {
                refreshInfoShownList(allInfoList);
            }
            else
            {
                Toast.makeText(MainActivity.this,"出现了问题，无法加载数据",Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
            super.onPostExecute(aBoolean);
        }
    }

    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }
}
