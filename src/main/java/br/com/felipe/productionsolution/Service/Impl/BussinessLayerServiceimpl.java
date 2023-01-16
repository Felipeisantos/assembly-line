package br.com.felipe.productionsolution.Service.Impl;

import br.com.felipe.productionsolution.Model.Task;
import br.com.felipe.productionsolution.Model.Time;
import br.com.felipe.productionsolution.Service.BussinessLayerService;
import br.com.felipe.productionsolution.Service.FileStorageService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BussinessLayerServiceimpl implements BussinessLayerService {
    @Autowired
    private FileStorageService fileStorageService;

    public String applyRulesOnFile(@NotNull MultipartFile file) {
        String[] rows = fileStorageService.readyFile(file).split("\n");

        List<Task> tasks = Arrays.stream(rows).map(this::convertLineToTask).filter(list -> list != null).collect(Collectors.toList());

        tasks.sort(Comparator.comparing(Task::getTime).reversed());

        while (tasks.isEmpty()){

        }

        return tasks.toString();
    }

    private Integer seachForBestTime(List<Task> tarefas, Time time, Integer jobShift) {
        Integer indexBestTime = null;
        if (jobShift == 1) {
            Time timeAux = time;
            for (Task tarefa : tarefas) {


                timeAux.addMinutes(tarefa.getTime());
                if (timeAux.getHours() < 12) {
                    return tarefa.getId();
                } else {
                    if (timeAux.getHours() == 12 || timeAux.getMinutes() == 00) {
                        tarefa.getId();
                    }
                }
            }
        }
        return indexBestTime;
    }

    private Task convertLineToTask(@NotNull String line) {
        String words[] = line.split(" ");
        if (words.length != 0) {
            Integer indexTime = searchTimeStringIndex(words);
            if (indexTime != null) {
                Task task = new Task();

                task.setTime(safeConversionTime(words[indexTime]));

                String description = "";
                for (int i = 0; i <= words.length - 1; i++) {
                    if (!words[indexTime].equals(words[i]))
                        description += " " + words[i];
                }
                task.setDescription(description);
                return task;
            }
        }

        return null;
    }

    public Integer searchTimeStringIndex(@NotNull String words[]) {
        int i = 0;
        for (String word : words) {
            if (word.toLowerCase().contains("min"))
                return i;
            i++;
        }
        return null;
    }

    public Integer safeConversionTime(@NotNull String str) {
        try {
            str = str.replaceAll("[^0-9]", "");
            if (!str.isEmpty())
                return Integer.valueOf(str);
        } catch (ConversionException e) {
            e.getMessage();
            return null;
        }
        return null;
    }
}
