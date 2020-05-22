package com.lesent.activiti.business.service;

import com.lesent.activiti.business.ActivitiApplication;
import com.lsdk.activiti.business.ActivitiApplication;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = ActivitiApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class TaskList {

    @Autowired
    private TaskService taskService;

    @Test
    public void TestTask() {
        List<Task> taskList = taskService.createTaskQuery()
                .taskNameIn(Arrays.asList("申请","执行"))
                .or()
                .taskAssignee("李涛")
                .taskCandidateUser("老张")
                .endOr()
                .list();
        System.out.println("taskList：" + taskList.size());
    }

}
