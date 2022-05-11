/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fuzzy.aycd.autosolve.AbstractAutoSolveManager
 *  com.fuzzy.aycd.autosolve.model.AutoSolveStatus
 *  com.fuzzy.aycd.autosolve.model.task.impl.CaptchaToken
 *  com.fuzzy.aycd.autosolve.model.task.impl.CaptchaTokenRequest
 *  okhttp3.OkHttpClient$Builder
 */
package io.trickle.harvester.autosolve;

import com.fuzzy.aycd.autosolve.AbstractAutoSolveManager;
import com.fuzzy.aycd.autosolve.model.AutoSolveStatus;
import com.fuzzy.aycd.autosolve.model.task.impl.CaptchaToken;
import com.fuzzy.aycd.autosolve.model.task.impl.CaptchaTokenRequest;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;

public class AutoSolve
extends AbstractAutoSolveManager {
    public void onStatusChanged(AutoSolveStatus autoSolveStatus) {
    }

    public AutoSolve(String string) {
        super(string);
    }

    public void onCaptchaTokenRequestCancelled(CaptchaTokenRequest captchaTokenRequest) {
    }

    public AutoSolve(OkHttpClient.Builder builder, String string) {
        super(builder, string);
    }

    public void onCaptchaTokenReceived(CaptchaToken captchaToken) {
    }

    public Logger getLogger() {
        return super.getLogger();
    }
}
