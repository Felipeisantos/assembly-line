package br.com.felipe.productionsolution.Service.Impl;

import br.com.felipe.productionsolution.Model.Task;
import br.com.felipe.productionsolution.Service.BussinessLayerService;
import br.com.felipe.productionsolution.Service.FileService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BussinessLayerServiceimpl implements BussinessLayerService {
    @Autowired
    private FileService fileService;
    private final Integer MAINTENANCE = 5;
    private final double TIMEASSEMBLYLINE = 360.00;

    public Map<Integer, List<Task>> divideAssemblyLine(List<Task> tasks, Integer numberAssemblyLine) {
        Map<Integer, Integer> timeListCount = fillTimeListCount(tasks);
        Map<Integer, List<Task>> assemblyLines = new HashMap<>();
        Map<Integer, Integer> timeListCountAux = new HashMap<>(timeListCount);
        for (var entry : timeListCount.entrySet()) {
            int divideAssemblyLine = (int) Math.floor(entry.getValue() / numberAssemblyLine);
            if (entry.getValue() % numberAssemblyLine == 0) {
                while (divideAssemblyLine != 0) {
                    IntStream.rangeClosed(0, numberAssemblyLine - 1).forEach(i -> assemblyLines.put(i, addAssemblyLines(assemblyLines.get(i), tasks, entry.getKey())));
                    divideAssemblyLine--;
                }
                timeListCountAux.remove(entry.getKey());
            } else {
                while (divideAssemblyLine != 0 && divideAssemblyLine != 1) {
                    IntStream.rangeClosed(0, numberAssemblyLine - 1).forEach(i -> assemblyLines.put(i, addAssemblyLines(assemblyLines.get(i), tasks, entry.getKey())));
                    divideAssemblyLine--;
                }
                timeListCountAux.remove(entry.getKey());
                timeListCountAux.put(entry.getKey(), entry.getValue() % numberAssemblyLine);

            }
        }
        //calculate remaining time
        if (!timeListCountAux.isEmpty()) {
            int i = 0;
            for (var entry : timeListCountAux.entrySet()) {
                Integer index = getMinTimeSumIndex(assemblyLines);
                List<Task> tasksMap = assemblyLines.get(index);
                if (entry.getKey() + getTotalTimeList(tasksMap) < (TIMEASSEMBLYLINE + 59))
                    assemblyLines.put(i, addAssemblyLines(tasksMap, tasks, entry.getKey()));
            }
        }

        return assemblyLines;
    }


    public String applyRulesOnFile(@NotNull MultipartFile file) {

        String[] rows = fileService.readyFile(file).split("\n");

        List<Task> tasks = Arrays.stream(rows).map(this::convertLineToTask).filter(Objects::nonNull).collect(Collectors.toList());

        Integer numberAssemblyLine = Math.toIntExact(Math.round(getTotalTimeList(tasks) / TIMEASSEMBLYLINE));


        Map<Integer, List<Task>> assemblyLines = divideAssemblyLine(tasks, numberAssemblyLine);

        for (var assemblyLine: assemblyLines.entrySet()) {


        }

        return tasks.toString();
    }

    private Task convertLineToTask(@NotNull String line) {
        boolean maintenance = line.contains("maintenance");
        String[] words = line.split(" ");
        if (words.length != 0) {
            Integer indexTime = searchTimeStringIndex(words);
            if (indexTime != null || maintenance) {
                Task task = new Task();

                if (maintenance)
                    task.setTime(MAINTENANCE);
                else
                    task.setTime(safeConversionTime(words[indexTime]));

                StringBuilder description = new StringBuilder();
                for (int i = 0; i <= words.length - 1; i++) {
                    if (!words[indexTime].equals(words[i]))
                        description.append(" ").append(words[i]);
                }
                if (maintenance)
                    task.setDescription(description.toString().replace("- maintenance", ""));
                else
                    task.setDescription(description.toString());
                return task;
            }
        }
        return null;
    }

    public Integer getMinTimeSumIndex(Map<Integer, List<Task>> assemblyLines) {
        int minTimeSum = assemblyLines.values().stream()
                .mapToInt(tasks -> tasks.stream().mapToInt(Task::getTime).sum())
                .min().orElse(0);
        return assemblyLines.entrySet().stream()
                .filter(entry -> entry.getValue().stream().mapToInt(Task::getTime).sum() == minTimeSum)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    //removing by reference
    public List<Task> addAssemblyLines(List<Task> tasksMap, List<Task> tasks, int key) {
        if (tasksMap == null)
            tasksMap = new ArrayList<>();
        Task task = tasks.stream().filter(task1 -> key == task1.getTime()).findFirst().orElse(null);
        tasks.remove(task);
        tasksMap.add(task);
        return tasksMap;
    }

    public int getTotalTimeList(List<Task> tasks) {
        return tasks.stream()
                .mapToInt(Task::getTime)
                .sum();
    }

    public Map<Integer, Integer> fillTimeListCount(List<Task> tasks) {
        Map<Integer, Integer> timeListCount = new HashMap<>();
        for (Task task : tasks) {
            if (timeListCount.containsKey(task.getTime()))
                timeListCount.put(task.getTime(), timeListCount.get(task.getTime()) + 1);
            else
                timeListCount.put(task.getTime(), 1);
        }
        return timeListCount;
    }
    public Integer searchTimeStringIndex(@NotNull String[] words) {
        int i = 0;
        for (String word : words) {
            if (word.toLowerCase().contains("min") || word.toLowerCase().contains("maintenance"))
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
