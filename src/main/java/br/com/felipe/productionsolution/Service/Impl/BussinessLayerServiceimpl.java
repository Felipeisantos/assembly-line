package br.com.felipe.productionsolution.Service.Impl;

import br.com.felipe.productionsolution.Model.Task;
import br.com.felipe.productionsolution.Model.Time;
import br.com.felipe.productionsolution.Service.BussinessLayerService;
import br.com.felipe.productionsolution.Service.FileService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class BussinessLayerServiceimpl implements BussinessLayerService {
    @Autowired
    private FileService fileService;
    private final double timeAssemblyLine = 410;

    private final Time morningStart = new Time(9, 0);

    private final Time lunchStart = new Time(12, 0);

    private final Time afternoonStart = new Time(13, 0);
    private final Time gymnasticsStart = new Time(16, 55);

    Integer morningTime = morningStart.getMinuteDifference(lunchStart);
    Integer afternoonTime = afternoonStart.getMinuteDifference(gymnasticsStart);

    public String applyRulesOnFile(@NotNull MultipartFile file) {

        String[] rows = fileService.readyFile(file).split("\n");

        List<Task> tasks = Arrays.stream(rows).map(this::convertLineToTask).filter(Objects::nonNull).collect(Collectors.toList());

        Integer numberAssemblyLine = Math.toIntExact(Math.round(getTotalTimeList(tasks) / timeAssemblyLine));

        Map<Integer, List<Task>> assemblyLines = divideAssemblyLine(tasks, numberAssemblyLine);

        List<Task> tasksList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        for (var assemblyLine : assemblyLines.entrySet()) {
            Time time = new Time(morningStart);
            List<Task> tasksAux = new ArrayList<>(assemblyLine.getValue());
            tasksList = setBestTask(tasksAux, time);
            stringBuilder.append("\nLinha de Montagem " + (assemblyLine.getKey() + 1) + "\n\n");
            for (Task task : tasksList) {
                stringBuilder.append(task.formatedTaskOutput());
            }
        }
        return stringBuilder.toString();
    }

    public List<Task> addAssemblyLines(List<Task> tasksMap, List<Task> tasks, int key) {
        if (tasksMap == null) tasksMap = new ArrayList<>();
        Task task = null;

        for (Task task1 : tasks) {
            if (key == task1.getTime()) {
                task = task1;
                break;
            }
        }
        tasksMap.add(task);
        tasks.remove(task);
        return tasksMap;
    }

    public Map<Integer, List<Task>> divideAssemblyLine(List<Task> tasks, Integer numberAssemblyLine) {
        Map<Integer, Integer> timeListCount = fillTimeListCount(tasks);
        Map<Integer, List<Task>> assemblyLines = new HashMap<>();
        Map<Integer, Integer> timeListCountAux = new HashMap<>(timeListCount);
        for (var entry : timeListCount.entrySet()) {
            int divideAssemblyLine = (int) Math.floor(entry.getValue() / numberAssemblyLine);
            if (entry.getValue() % numberAssemblyLine == 0) {
                while (divideAssemblyLine != 0) {
                    int bound = numberAssemblyLine - 1;
                    for (int i = 0; i <= bound; i++)
                        assemblyLines.put(i, addAssemblyLines(assemblyLines.get(i), tasks, entry.getKey()));
                    divideAssemblyLine--;
                }
                timeListCountAux.remove(entry.getKey());
            } else {
                while (divideAssemblyLine != 0) {
                    int bound = numberAssemblyLine - 1;
                    for (int i = 0; i <= bound; i++)
                        assemblyLines.put(i, addAssemblyLines(assemblyLines.get(i), tasks, entry.getKey()));
                    divideAssemblyLine--;
                }
                timeListCountAux.remove(entry.getKey());
                timeListCountAux.put(entry.getKey(), entry.getValue() % numberAssemblyLine);
            }
        }
        //calculate remaining time
        if (!timeListCountAux.isEmpty()) {
            for (var entry : timeListCountAux.entrySet()) {
                Integer index = getMinTimeSumIndex(assemblyLines);
                List<Task> tasksMap = new ArrayList<>(assemblyLines.get(index));
                if (entry.getKey() + getTotalTimeList(tasksMap) < timeAssemblyLine)
                    assemblyLines.put(index, new ArrayList<>(addAssemblyLines(tasksMap, tasks, entry.getKey())));
            }
        }

        return assemblyLines;
    }

    private @NotNull List<Task> setBestTask(List<Task> tasks, Time currentTime) {
        Time timeAux = new Time(currentTime);
        List<Task> tasksOptimized = new ArrayList<>();
        int morningTimeAux = morningTime;
        while (morningTimeAux != 0) {
            Task maxTimeTask = tasks.stream().max(Comparator.comparing(Task::getTime)).orElse(null);
            timeAux.addMinutes(maxTimeTask.getTime());
            if ((timeAux.isLesser(lunchStart))) {
                int time = timeAux.getMinuteDifference(afternoonStart);
                List<Task> tasks1 = canBeDivided(tasks, time);
                if (tasks1 != null) {
                    for (Task task : tasks1) {
                        task.setExecutionTime(currentTime);
                        currentTime.addMinutes(task.getTime());
                        tasks.remove(task);
                        morningTimeAux -= task.getTime();
                        tasksOptimized.add(task);
                    }
                } else {
                    maxTimeTask.setExecutionTime(currentTime);
                    currentTime.addMinutes(maxTimeTask.getTime());
                    tasks.remove(maxTimeTask);
                    morningTimeAux -= maxTimeTask.getTime();
                    tasksOptimized.add(maxTimeTask);
                }
            }

        }
        Task task1 = new Task(" Almoço", 60);
        currentTime.setHours(12);
        currentTime.setMinutes(0);
        task1.setExecutionTime(currentTime);
        currentTime.addMinutes(task1.getTime());
        tasksOptimized.add(task1);
        while (tasks.size() > 0) {
            Task maxTimeTask = tasks.stream().max(Comparator.comparing(Task::getTime)).orElse(null);
            timeAux.addMinutes(maxTimeTask.getTime());
            if ((timeAux.isLesser(gymnasticsStart))) {
                int time = timeAux.getMinuteDifference(gymnasticsStart);
                List<Task> tasks1 = canBeDivided(tasks, time);
                if (tasks1 != null) {
                    for (Task task : tasks1) {
                        task.setExecutionTime(currentTime);
                        currentTime.addMinutes(task.getTime());
                        tasks.remove(task);
                        tasksOptimized.add(task);
                    }
                } else {
                    maxTimeTask.setExecutionTime(currentTime);
                    currentTime.addMinutes(maxTimeTask.getTime());
                    tasks.remove(maxTimeTask);
                    tasksOptimized.add(maxTimeTask);
                }
            }

        }

        task1 = new Task(" Ginástica laboral", 0);
        task1.setExecutionTime(currentTime);
        currentTime.addMinutes(task1.getTime());
        tasksOptimized.add(task1);


        return tasksOptimized;
    }

    public List<Task> canBeDivided(@NotNull List<Task> tasks, int remainingTime) {
        List<Task> divisibleTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTime() <= remainingTime / 2 && task.getTime() % 2 == 0) {
                divisibleTasks.add(task);
            }
            if (divisibleTasks.stream().mapToInt(Task::getTime).sum() == remainingTime) return divisibleTasks;
        }
        return null;
    }

    private @Nullable Task convertLineToTask(@NotNull String line) {
        boolean isMaintenance = line.contains("maintenance");
        String[] words = line.split(" ");
        if (words.length != 0) {
            Integer indexTime = searchTimeStringIndex(words);
            if (indexTime != null || isMaintenance) {
                Task task = new Task();

                Integer maintenance = 5;
                if (isMaintenance) task.setTime(maintenance);
                else task.setTime(safeConversionTime(words[indexTime]));

                StringBuilder description = new StringBuilder();
                for (int i = 0; i <= words.length - 1; i++) {
                    if (!words[indexTime].equals(words[i])) description.append(" ").append(words[i]);
                }
                if (isMaintenance) task.setDescription(description.toString().replace("- maintenance", ""));
                else task.setDescription(description.toString());
                return task;
            }
        }
        return null;
    }

    public Integer getMinTimeSumIndex(@NotNull Map<Integer, List<Task>> assemblyLines) {
        int minTimeSum = assemblyLines.values().stream().mapToInt(tasks -> tasks.stream().mapToInt(Task::getTime).sum()).min().orElse(0);
        return assemblyLines.entrySet().stream().filter(entry -> entry.getValue().stream().mapToInt(Task::getTime).sum() == minTimeSum).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    //removing by reference


    public int getTotalTimeList(@NotNull List<Task> tasks) {
        return tasks.stream().mapToInt(Task::getTime).sum();
    }

    public Map<Integer, Integer> fillTimeListCount(@NotNull List<Task> tasks) {
        Map<Integer, Integer> timeListCount = new HashMap<>();
        for (Task task : tasks) {
            if (timeListCount.containsKey(task.getTime()))
                timeListCount.put(task.getTime(), timeListCount.get(task.getTime()) + 1);
            else timeListCount.put(task.getTime(), 1);
        }
        return timeListCount;
    }

    public Integer searchTimeStringIndex(String @NotNull [] words) {
        int i = 0;
        for (String word : words) {
            if (word.toLowerCase().contains("min") || word.toLowerCase().contains("maintenance")) return i;
            i++;
        }
        return null;
    }

    public Integer safeConversionTime(@NotNull String str) {
        String numbersOnly = str.replaceAll("[^0-9]", "");
        return numbersOnly.isEmpty() ? null : Integer.parseInt(numbersOnly);
    }
}
