package lzhou.spring.batch.batchutils.cmd;

import org.junit.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by lzhou on 9/15/2017.
 */
public class JsonBatchConfigurerTest {
    @Test
    public void test_JsonBatchConfigurer_1() {
        BatchCmdConfigurer configurer = new JsonBatchCmdConfigurer("[{\"jobName\":\"j1\", \"contextFile\":\"1.xml\", \"parameters\":[]}, {\"jobName\":\"j2\", \"contextFile\":\"2.xml\", \"parameters\":[\"p1\", \"p2\"]},]");
        configurer.parseArgs("-j j1".split(" "));
        assertThat(configurer.getJobName(), is("j1"));
        assertThat(configurer.getContextFile(), is("1.xml"));
        assertThat(configurer.getParameters().size(), is(0));
    }
    @Test
    public void test_JsonBatchConfigurer_2() {
        BatchCmdConfigurer configurer = new JsonBatchCmdConfigurer("[{\"jobName\":\"j1\", \"contextFile\":\"1.xml\", \"parameters\":[]}, {\"jobName\":\"j2\", \"contextFile\":\"2.xml\", \"parameters\":[\"p1\", \"p2\"]},]");
        configurer.parseArgs("-j j2 -p a=b c=b e=f".split(" "));
        assertThat(configurer.getJobName(), is("j2"));
        assertThat(configurer.getContextFile(), is("2.xml"));
        assertThat(configurer.getParameters().size(), is(3));
        assertThat(configurer.getParameters().get("a"), is("b"));
        assertThat(configurer.getParameters().get("c"), is("b"));
        assertThat(configurer.getParameters().get("e"), is("f"));
    }
}
