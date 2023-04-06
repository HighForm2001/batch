package com.batch_drop.batch.components;

import com.batch_drop.batch.entity.TestObject;
import com.batch_drop.batch.entity.Transaction;
import com.batch_drop.batch.repositories.TestObjectRepository;
import com.batch_drop.batch.repositories.TransactionRepository;
import com.batch_drop.batch.util.TestMapper;
import com.batch_drop.batch.util.TransactionMapper;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.StoredProcedureItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

@Component
public class ReaderComponent {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TestObjectRepository testObjectRepository;

    @Bean
    @StepScope
    public JdbcCursorItemReader<Transaction> itemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Transaction>()
                .dataSource(dataSource)
                .name("transactionReader")
                .sql("SELECT t.transaction_reference, t.amount, t.currency, t.transaction_id ," +
                        " t.transaction_date, t.from_acc_id, t.to_acc_id FROM transaction_t t")
                .rowMapper(new TransactionMapper())
                .build();
    }
    @Bean
    @StepScope
    public JdbcCursorItemReader<TestObject> testReader(DataSource dataSource){
        return new JdbcCursorItemReaderBuilder<TestObject>()
                .dataSource(dataSource)
                .name("testObject Reader")
                .sql("SELECT id,value from test_object_t")
                .rowMapper(new TestMapper())
                .build();
    }
    @Bean
    @StepScope
    public StoredProcedureItemReader<TestObject> test_stored_reader(DataSource dataSource){
        StoredProcedureItemReader<TestObject> reader = new StoredProcedureItemReader<>();
        reader.setDataSource(dataSource);
        reader.setProcedureName("test_object");
        reader.setRowMapper(new TestMapper());
        return reader;
    }
    @Bean
    @StepScope
    public StoredProcedureItemReader<Transaction> storedProReader(DataSource dataSource){

        SqlParameter[] parameterList = new SqlParameter[]
                {new SqlParameter("intCreator", Types.INTEGER)};
        StoredProcedureItemReader<Transaction> reader = new StoredProcedureItemReader<>();
        reader.setDataSource(dataSource);
        reader.setProcedureName("test_function3");
        reader.setParameters(parameterList);
        reader.setRowMapper(new TransactionMapper());
        reader.setPreparedStatementSetter(statementSetter());
        return reader;
    }
    public PreparedStatementSetter statementSetter(){
        return ps -> ps.setInt(1,3);
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamReader<Transaction> synchronizedReader(DataSource dataSource){
        StoredProcedureItemReader<Transaction> spReader = storedProReader(dataSource);
        SynchronizedItemStreamReader<Transaction> syncReader = new SynchronizedItemStreamReader<>();
        syncReader.setDelegate(spReader);
        return syncReader;
    }
    @Bean
    @StepScope
    public RepositoryItemReader<Transaction> repositoryItemReader(){
        RepositoryItemReaderBuilder<Transaction> repositoryItemReaderBuilder = new RepositoryItemReaderBuilder<>();
        repositoryItemReaderBuilder.name("Repository Item Reader");
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("amount",Sort.Direction.DESC);
        sorts.put("transaction_reference",Sort.Direction.ASC);
        repositoryItemReaderBuilder.sorts(sorts);
//        repositoryItemReaderBuilder.pageSize(4);
//        repositoryItemReaderBuilder.maxItemCount(3);
        repositoryItemReaderBuilder.repository(transactionRepository);
//        repositoryItemReaderBuilder.saveState(false);
//        repositoryItemReaderBuilder.methodName("findAll");
        repositoryItemReaderBuilder.methodName("findCustom");
        return repositoryItemReaderBuilder.build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<TestObject> testRepoReader(){
        RepositoryItemReaderBuilder<TestObject> builder = new RepositoryItemReaderBuilder<>();
        builder.name("Test Repo");
        Map<String,Sort.Direction> sorts = new HashMap<>();
        sorts.put("id",Sort.Direction.ASC);
        builder.sorts(sorts);
        builder.pageSize(2);
        builder.repository(testObjectRepository);
        builder.methodName("findAll");
        return builder.build();

    }
}
