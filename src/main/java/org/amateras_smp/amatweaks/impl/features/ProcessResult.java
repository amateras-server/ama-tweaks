package org.amateras_smp.amatweaks.impl.features;

public class ProcessResult {
    public final boolean cancelProcessing;
    public final boolean closeGui;

    public ProcessResult(boolean cancelProcessing, boolean closeGui) {
        this.cancelProcessing = cancelProcessing;
        this.closeGui = closeGui;
    }

    public static ProcessResult skipped() {
        return new ProcessResult(false, false);
    }

    public static ProcessResult terminated() {
        return new ProcessResult(true, true);
    }
}
