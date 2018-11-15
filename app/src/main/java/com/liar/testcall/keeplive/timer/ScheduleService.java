package com.liar.testcall.keeplive.timer;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.liar.testcall.service.MainService;
import com.liar.testcall.utils.service.ServiceUtils;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
/**
 * JobService，定时服务用于唤醒MainService
 */

public class ScheduleService extends JobService {
    private static final String TAG = "ScheduleService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "JobService启动：：onStartJob(): params = [" + params + "]");
        if(!ServiceUtils.isServiceRunning(getApplication(),MainService.class.getName())){
            Intent intent = new Intent(getApplicationContext(), MainService.class);
            startService(intent);
        }
        jobFinished(params, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob(): params = [" + params + "]");
        return false;
    }
}
