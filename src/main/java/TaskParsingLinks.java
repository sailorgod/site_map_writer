import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

@Slf4j
public class TaskParsingLinks extends RecursiveTask<String>{

    private final String url;
    private final String startLink;
    private final String tab;
    private final List<TaskParsingLinks> taskList;
    private final Map<String, Integer> linkMarker;

    public TaskParsingLinks(String url) {
        this.url = url;
        taskList = new ArrayList<>();
        linkMarker = new HashMap<>();
        startLink = url;
        tab = "";
    }

    private TaskParsingLinks(String url, String startLink, Map<String, Integer> linkMarker, String tab){
        this.url = url;
        this.startLink = startLink;
        this.linkMarker = linkMarker;
        this.tab = "\t" + tab;
        taskList = new ArrayList<>();
    }

    @Override
    protected String compute() {

        StringBuffer buffer = new StringBuffer();
        List<String> urlList = null;
        buffer.append(tab + url + "\n");
        log.info("Link:" + tab + url);

        try {
            urlList = SiteParser.parse(url);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        if(urlList != null && !urlList.isEmpty()) {

            for (String url : urlList) {
                if(linkMarker.containsKey(url)) {
                    continue;
                }
                linkMarker.put(url, 0);
            }

            urlList.forEach(u -> {
                if(!task(u).equals("")){
                    buffer.append(task(u) + "\n");
                }
            });
        }

        for (TaskParsingLinks task: taskList) {

            buffer.append(task.join());

        }

        return buffer.toString();
    }

    private String task(String url) {

        if(linkMarker.get(url) != 0 || !url.contains(startLink)) {
            return "";
        }
        linkMarker.put(url, 1);
        String result = tab + url;
        log.info("Link: " + tab + url);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Sleep");
        TaskParsingLinks task = new TaskParsingLinks(url, startLink, linkMarker, tab);
        taskList.add(task);
        task.fork();
        log.info("Task fork");

        return result;

    }

}
