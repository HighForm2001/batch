package com.batch_drop.batch.util;

import com.batch_drop.batch.repositories.TransactionRepository;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

public class RecordPartitioner implements Partitioner {
    private TransactionRepository repository;

    public RecordPartitioner(TransactionRepository repository){
        this.repository = repository;
    }
    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String,ExecutionContext> map = new HashMap<>();
        int range = 10;
        int fromId = 1;
        int toId = range;
        for(int i = fromId; i<= gridSize;){
            ExecutionContext context = new ExecutionContext();
            context.putInt("fromID",fromId);
            context.putInt("toId",toId);
            context.putString("name","Thread "+i);
            context.putString("outputFilePath","part-"+i);
            map.put("partition " + i,context);
            fromId = toId+1;
            i = fromId;
            toId += range;
        }
        return map;
    }
}
