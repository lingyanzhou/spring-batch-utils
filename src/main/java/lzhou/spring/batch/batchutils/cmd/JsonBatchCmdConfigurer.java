package lzhou.spring.batch.batchutils.cmd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.cli.*;
import org.apache.commons.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzhou on 9/14/2017.
 */
public class JsonBatchCmdConfigurer implements BatchCmdConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(JsonBatchCmdConfigurer.class);


    private String jobName = "";

    private Map<String, String> parameters = new HashMap<String,String>();

    private List<BatchJobExecInfo> batchJobExecInfoList = new ArrayList<BatchJobExecInfo>();
    private Options options = null;

    public String getJobName() {
        return jobName;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
    public String getContextFile() {
        for (BatchJobExecInfo info: batchJobExecInfoList) {
            if (info.getJobName().equalsIgnoreCase(jobName)) {
                return info.getContextFile();
            }
        }
        return "";
    }

    public JsonBatchCmdConfigurer(String json) {
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<ArrayList<BatchJobExecInfo>>(){}.getType();
        batchJobExecInfoList = gson.fromJson(json, listType);
    }

    public Status parseArgs(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = makeOptions();
        CommandLine cmd = null;
        try{
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar somejar ", "", options, makeFooter() , true);
            return Status.CMD_ERROR;
        }


        if (cmd.hasOption("h")){
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar somejar", "", options, makeFooter() , true);
            return Status.EXIT_REQUESTED;
        }


        jobName = cmd.getOptionValue("j");
        if (cmd.hasOption("p")) {
            for (String p : cmd.getOptionValues("p")) {
                String[] fields = p.split("=", 2);
                parameters.put(fields[0], fields[1]);
            }
        }

        return Status.PROCEED;
    }

    private Options makeOptions() {
        Options options = new Options();

        Option help = Option.builder("h").longOpt("help").desc("print help page").build();
        Option job = Option.builder("j").longOpt("job").desc("print help page").hasArgs().required().build();
        Option param = Option.builder("p").longOpt("parameter").desc("print help page").required(false).hasArgs().build();
        options.addOption(help);
        options.addOption(job);
        options.addOption(param);

        return options;
    }

    private String makeFooter() {
        if (null==batchJobExecInfoList) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (BatchJobExecInfo info: batchJobExecInfoList) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("jobName", info.getJobName());
            map.put("contextFile", info.getContextFile());
            map.put("parameters", info.getParameters().toString());
            StrSubstitutor substitutor = new StrSubstitutor(map);
            if (info.getParameters() == null || info.getParameters().size() == 0) {
                sb.append(substitutor.replace("Job: ${jobName}, parameters: ${parameters}, context: ${contextFile}. Cmd: -j ${jobName}\n"));
            } else {
                sb.append(substitutor.replace("Job: ${jobName}, parameters: ${parameters}, context: ${contextFile}. Cmd: -j ${jobName} -p ${parameters}\n"));
            }
        }
        return sb.toString();
    }
}