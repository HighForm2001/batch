package com.batch_drop.batch.tasks;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import javax.swing.*;
import java.time.LocalTime;

public class MyTaskOne implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("MyTaskOne start...");
        //executions
        System.out.println("MyTaskOne done...");
        return RepeatStatus.continueIf(LocalTime.now().getMinute()==3);
    }
}
