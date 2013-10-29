package edu.vt.teja.ece4564.misplace;

import java.util.Locale;

import android.location.Location;
import android.location.LocationManager;
import android.nfc.Tag;
import android.nfc.NdefMessage;
import android.nfc.FormatException;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;



    private NfcAdapter nfcAdapter_;
    private PendingIntent nfcPendingIntent_;
    private IntentFilter[] writeTagFilters_;
    private IntentFilter[] ndefExchangeFilters_;
    public static TextView textViewAddTagID_;
    public static MessageTask messageTask_;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

        nfcAdapter_ = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent_ = PendingIntent.getActivity(this, 0,
                new Intent(this, ((Object) this).getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Intent filters for reading a note from a tag or exchanging over p2p.
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) { }
        ndefExchangeFilters_ = new IntentFilter[] { ndefDetected };

        // Intent filters for writing to a tag
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        writeTagFilters_ = new IntentFilter[] { tagDetected };
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}


	// NFC function calls for various instances
	@Override
    protected void onResume() {
        super.onResume();
        //mResumed = true;
        // Sticky notes received from Android
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {

        }
        enableNdefExchangeMode();
    }

    @Override
    protected void onPause() {
    	byte data[] = new byte[4];
        super.onPause();
        //mResumed = false;
        try {
			nfcAdapter_.setNdefPushMessage(new NdefMessage(data), this);
		} catch (FormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())){
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            textViewAddTagID_.setText(detectedTag.getId().toString());
        }
    }

    private void enableNdefExchangeMode() {
        byte data[] = new byte[4];
        try {
            nfcAdapter_.setNdefPushMessage(new NdefMessage(data), this);
        } catch (FormatException e) {
            e.printStackTrace();
        }
        nfcAdapter_.enableForegroundDispatch(this, nfcPendingIntent_, ndefExchangeFilters_, null);
    }


	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			switch (position){
                case 0:
                    fragment = new AddTagsFragment();
                    break;
                /*case 1:
                    fragment = new CheckinTagsFragment();
                    break;*/
                case 1:
                    fragment = new QueryTagsFragment();
                    break;
            }
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section3).toUpperCase(l);
			case 2:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}


//Fragments

    public static class AddTagsFragment extends Fragment implements OnClickListener{
        private Button btnAddTag_;
        private TextView messageText;
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_addtags, container, false);
            textViewAddTagID_ = (TextView) rootView.findViewById(R.id.textView_tagID);
            btnAddTag_ = (Button) rootView.findViewById(R.id.button_addTag);
            messageText = (TextView) rootView.findViewById(R.id.editText_newTagMessage);
            btnAddTag_.setOnClickListener(this);
            return rootView;
        }
        
        @Override
        public void onClick(View v) {
        	messageTask_ = new MessageTask(getActivity());
        	Time curTime = new Time();
        	curTime.setToNow();
        	LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        	messageTask_.setMessage(textViewAddTagID_.getText().toString(), messageText.getText().toString(), curTime.toString(), location.getLatitude(), location.getLongitude());
        	messageTask_.execute("add");
        }
    }

    public static class CheckinTagsFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_checkintags, container, false);
            
            return rootView;
        }
    }

    public static class QueryTagsFragment extends Fragment implements OnClickListener {
    	private TextView queryText;
    	private Button queryBtn;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_querytags, container, false);
            
            queryBtn = (Button) rootView.findViewById(R.id.button_query);
            queryText = (TextView) rootView.findViewById(R.id.queryText);
            queryBtn.setOnClickListener(this);
            
            
            return rootView;
        }
        
        @Override
        public void onClick(View v) {
        	messageTask_ = new MessageTask(getActivity());
        	messageTask_.setQueryText(queryText);
        	messageTask_.execute("get");
        }
    }

}
