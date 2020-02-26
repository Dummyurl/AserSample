package info.pratham.asersample.fragments.subject;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import info.pratham.asersample.R;
import info.pratham.asersample.fragments.nativeFragments.Paragraph;
import info.pratham.asersample.fragments.nativeFragments.Words;
import info.pratham.asersample.interfaces.CheckQuestionListener;
import info.pratham.asersample.interfaces.RecordPrepairListner;

public class NativeLang extends Fragment{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    CheckQuestionListener checkQuestionListener;
    ViewPagerAdapter adapter;
    TabLayout.Tab mTab;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        checkQuestionListener = (CheckQuestionListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.native_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setCurrentItem(1);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //set words as a default
        TabLayout.Tab tab = tabLayout.getTabAt(2);
        tab.select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //do stuff here
                //  mTab = tab;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mTab = tab;
                showCheckAnswerDialog("inFragment");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void showCheckAnswerDialog(String flag) {
        Fragment fragment;
        if (flag.equals("inFragment")) {
            fragment = adapter.getItem(mTab.getPosition());
        } else {
            mTab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
            fragment = adapter.getItem(mTab.getPosition());
        }

        if (fragment instanceof Words) {
            Words words = (Words) fragment;
            words.backupList();
        } else if (fragment instanceof Paragraph) {
            Paragraph paragraph = (Paragraph) fragment;
            paragraph.backupList();
        }
        checkQuestionListener.showChekingDialog("Native", mTab.getText().toString(), flag);
        // Toast.makeText(getActivity(), "done" + mTab.getText(), Toast.LENGTH_SHORT).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new Words(), "Letters");
        adapter.addFragment(new Words(), "Words");
        adapter.addFragment(new Paragraph(), "Para");
        adapter.addFragment(new Paragraph(), "Story");
        viewPager.setAdapter(adapter);

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            Bundle args = new Bundle();
            args.putString("level", title);
            fragment.setArguments(args);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}