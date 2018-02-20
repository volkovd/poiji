package com.poiji.deserialize;

import com.poiji.bind.Poiji;
import com.poiji.deserialize.model.Employee;
import com.poiji.deserialize.model.EmployeeComment;
import com.poiji.option.PoijiOptions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CommentTest {

    private String path;

    public CommentTest(String path) {
        super();
        this.path = path;
    }

    @Parameters
    public static Object[] excel() throws Exception {
        return new Object[]{"src/test/resources/testComment.xlsx"};
    }

    @Test
    public void shouldIgnoreComments() {
        PoijiOptions opts = PoijiOptions.PoijiOptionsBuilder.settings().commentFlag("#").build();
        List<EmployeeComment> actualEmployees = Poiji.fromExcel(new File(path), EmployeeComment.class, opts);
        assertThat(actualEmployees, notNullValue());
        assertThat(actualEmployees.size(), is(3));
    }

    @Test
    public void shouldIgnoreNonData() {
        PoijiOptions opts = PoijiOptions.PoijiOptionsBuilder.settings().commentFlag("#").dataFlag("D").build();
        List<EmployeeComment> actualEmployees = Poiji.fromExcel(new File(path), EmployeeComment.class, opts);
        assertThat(actualEmployees, notNullValue());
        assertThat(actualEmployees.size(), is(2));
    }
}
