package com.adisoftc.chaturanga.utils;

import android.app.Activity;
import android.content.IntentSender;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

/**
 * UpdateManager - Handles in-app update detection and installation
 * Uses Google Play In-App Updates API to check for app updates
 */
public class UpdateManager {
    
    private static final String TAG = "UpdateManager";
    public static final int UPDATE_REQUEST_CODE = 1001;
    
    private AppUpdateManager appUpdateManager;
    private Activity activity;
    
    public UpdateManager(Activity activity) {
        this.activity = activity;
        this.appUpdateManager = AppUpdateManagerFactory.create(activity);
    }
    
    /**
     * Check if an update is available on Google Play Store
     * Call this from onCreate() or onResume()
     */
    public void checkForUpdate() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                Log.d(TAG, "Update available!");
                
                // Get update priority (0-5, set in Play Console)
                int priority = appUpdateInfo.updatePriority();
                
                // Get days since update became available
                Integer stalenessDays = appUpdateInfo.clientVersionStalenessDays();
                
                // Decide update type based on priority and staleness
                if (shouldForceUpdate(priority, stalenessDays)) {
                    // Critical update - force immediate update
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        startImmediateUpdate(appUpdateInfo);
                    }
                } else {
                    // Optional update - offer flexible update
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                        startFlexibleUpdate(appUpdateInfo);
                    }
                }
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_NOT_AVAILABLE) {
                Log.d(TAG, "No update available. App is up to date.");
            }
        });
        
        appUpdateInfoTask.addOnFailureListener(exception -> {
            Log.e(TAG, "Failed to check for updates: " + exception.getMessage());
        });
    }
    
    /**
     * Determine if update should be forced
     * @param priority Update priority from Play Console (0-5)
     * @param stalenessDays Days since update became available
     * @return true if immediate update should be forced
     */
    private boolean shouldForceUpdate(int priority, Integer stalenessDays) {
        // Force immediate update if:
        // 1. Priority is 5 (critical security update)
        // 2. Priority is 4+ and app is 7+ days old
        // 3. App is 30+ days old (very outdated)
        
        if (priority >= 5) {
            return true; // Critical update
        }
        
        if (stalenessDays != null) {
            if (priority >= 4 && stalenessDays >= 7) {
                return true; // Important update after 7 days
            }
            if (stalenessDays >= 30) {
                return true; // Very old version
            }
        }
        
        return false;
    }
    
    /**
     * Start immediate update (fullscreen, blocking)
     * User must update to continue using the app
     */
    private void startImmediateUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.IMMEDIATE,
                activity,
                UPDATE_REQUEST_CODE
            );
            Log.d(TAG, "Starting immediate update...");
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Failed to start immediate update: " + e.getMessage());
        }
    }
    
    /**
     * Start flexible update (background download)
     * User can continue using app while update downloads
     */
    private void startFlexibleUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.FLEXIBLE,
                activity,
                UPDATE_REQUEST_CODE
            );
            Log.d(TAG, "Starting flexible update...");
            
            // Listen to download progress
            appUpdateManager.registerListener(state -> {
                if (state.installStatus() == InstallStatus.DOWNLOADED) {
                    // Update downloaded, prompt user to install
                    Log.d(TAG, "Update downloaded. Ready to install.");
                    popupSnackbarForCompleteUpdate();
                } else if (state.installStatus() == InstallStatus.DOWNLOADING) {
                    long bytesDownloaded = state.bytesDownloaded();
                    long totalBytes = state.totalBytesToDownload();
                    int progress = (int) ((bytesDownloaded * 100) / totalBytes);
                    Log.d(TAG, "Download progress: " + progress + "%");
                }
            });
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Failed to start flexible update: " + e.getMessage());
        }
    }
    
    /**
     * Show notification to user that update is downloaded
     * Call completeUpdate() when user taps to install
     */
    private void popupSnackbarForCompleteUpdate() {
        // You can show a Snackbar or Dialog here
        // For simplicity, we'll just complete the update
        appUpdateManager.completeUpdate();
    }
    
    /**
     * Resume update if it was in progress
     * Call this from onResume()
     */
    public void resumeUpdateIfNeeded() {
        appUpdateManager
            .getAppUpdateInfo()
            .addOnSuccessListener(appUpdateInfo -> {
                // If immediate update was in progress, continue it
                if (appUpdateInfo.updateAvailability() 
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    startImmediateUpdate(appUpdateInfo);
                }
                
                // If flexible update is downloaded, prompt to install
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate();
                }
            });
    }
    
    /**
     * Call this from Activity's onActivityResult()
     */
    public void handleActivityResult(int requestCode, int resultCode) {
        if (requestCode == UPDATE_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                Log.d(TAG, "Update flow failed or cancelled. Result code: " + resultCode);
                // Handle update failure (user cancelled or error occurred)
            } else {
                Log.d(TAG, "Update successful!");
            }
        }
    }
    
    /**
     * Unregister update listener
     * Call this from onDestroy() if using flexible updates
     */
    public void unregisterListener() {
        // Only needed if you registered a listener for flexible updates
        // appUpdateManager.unregisterListener(listener);
    }
}
