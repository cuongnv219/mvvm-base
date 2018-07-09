package com.base.utils.rx;

import io.reactivex.Scheduler;

/**
 * ka
 * 21/02/2018
 */

public interface SchedulerProvider {

    Scheduler ui();

    Scheduler computation();

    Scheduler io();
}
