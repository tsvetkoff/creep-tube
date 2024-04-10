package tsvetkoff.creep.type;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author SweetSupremum
 */
@Entity
@NoArgsConstructor
@Data
public class Event {
    @Id
    private Long id;
    private LocalDateTime eventDateTime;
    private String title;

    private static final int[] arr = new int[100];
    private static final BlockingQueue<int[]> batchQueue = new ArrayBlockingQueue<>(20);


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ScheduledExecutorService scheduledThreadPoolExecutor = Executors.newSingleThreadScheduledExecutor();

        executorService.execute(() -> {
            int[] tempArr = new int[5];
            int tempIndex = 0;
            for (int i = 0; i < arr.length; i++) {
                arr[i] = i;
                tempArr[tempIndex] = arr[i];
                tempIndex++;
                if (i != 0 && (i + 1) % 5 == 0) {
                    batchQueue.add(tempArr);
                    tempArr = new int[5];
                    tempIndex = 0;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            int[] poll = batchQueue.poll();
            if (poll != null) {
                for (int i = 0; i < poll.length; i++) {

                    System.out.println(poll[i]);
                    System.out.println("---------------------");

                }
                System.out.printf("Current thread"+Thread.currentThread());
            }

        }, 100, 100, TimeUnit.MILLISECONDS);

        batchQueue.add(new int[]{-10});


    }
}
