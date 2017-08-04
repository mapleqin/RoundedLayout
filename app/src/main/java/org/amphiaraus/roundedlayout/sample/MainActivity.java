package org.amphiaraus.roundedlayout.sample;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import org.amphiaraus.roundedlayout.RoundedLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.round_layout) RoundedLayout mRoundedLayout;
    @BindView(R.id.as_circle) Switch mAsCircleSwitch;
    @BindView(R.id.top_left) Switch mTopLeftSwitch;
    @BindView(R.id.top_right) Switch mTopRightSwitch;
    @BindView(R.id.bottom_right) Switch mBottomRightSwitch;
    @BindView(R.id.bottom_left) Switch mBottomLeftSwitch;
    @BindView(R.id.corner_radius) SeekBar mCornerRadiusSeekBar;
    @BindView(R.id.border_width) SeekBar mBorderWidthSeekBar;
    @BindView(R.id.elevation_seek) SeekBar mElevationSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAsCircleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mRoundedLayout.setRoundAsCircle(isChecked);
            }
        });
        mTopLeftSwitch.setOnCheckedChangeListener(this);
        mTopRightSwitch.setOnCheckedChangeListener(this);
        mBottomRightSwitch.setOnCheckedChangeListener(this);
        mBottomLeftSwitch.setOnCheckedChangeListener(this);

        mCornerRadiusSeekBar.setMax(dp2px(getResources(), 100));
        mCornerRadiusSeekBar.setProgress(dp2px(getResources(), 10));
        mCornerRadiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                adjustCornerRadius(progress);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mBorderWidthSeekBar.setMax(20);
        mBorderWidthSeekBar.setProgress(dp2px(getResources(), .5f));
        mBorderWidthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mRoundedLayout.setRoundingBorderWidth(progress);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mElevationSeekBar.setMax(dp2px(getResources(), 50));
        mElevationSeekBar.setProgress(dp2px(getResources(), 10));
        mElevationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mRoundedLayout.setRoundingElevation(progress);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void adjustCornerRadius(int cornerRadius) {
        mRoundedLayout.setRoundedCornerRadius(cornerRadius,
                mTopLeftSwitch.isChecked(), mTopRightSwitch.isChecked(),
                mBottomRightSwitch.isChecked(), mBottomLeftSwitch.isChecked());
    }

    @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        adjustCornerRadius(mCornerRadiusSeekBar.getProgress());
    }

    private static int dp2px(Resources resources, float value) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
    }
}
