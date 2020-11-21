package barber_shop_problem.solution_2;

import java.util.concurrent.*;

public class BarberShopSolution2 {

    public static class Barbershop {
        private final Barber barber;
        private final ExecutorService barbersService = Executors.newFixedThreadPool(1);
        private final ExecutorService customersService = Executors.newCachedThreadPool();
        private final ArrayBlockingQueue<Integer> seats = new ArrayBlockingQueue<>(3);
        private final SynchronousQueue<Object> haircutOperationPoint = new SynchronousQueue<>(true);

        public Barbershop() {
            barber = new Barber(haircutOperationPoint, seats);
        }

        public void startShop() {
            barbersService.submit(barber);
        }

        public void customerArrive() {
            customersService.submit(new Customer(haircutOperationPoint, seats));
        }

        public void closeShop() {
            barber.stopWorking();
            barbersService.shutdownNow();
            customersService.shutdownNow();
        }
    }

    public static class Barber implements Runnable {
        private SynchronousQueue<Object> haircutOperationPoint;
        private ArrayBlockingQueue<Integer> seats;
        private boolean isWorking = true;

        public Barber(final SynchronousQueue<Object> haircutOperationPoint, ArrayBlockingQueue<Integer> seats) {
            this.haircutOperationPoint = haircutOperationPoint;
            this.seats = seats;
        }

        public void cutHair() {
            System.out.println("Barber: Cutting Hair --- " + Thread.currentThread().getName());
        }

        public void leaveWorkplace() {
            System.out.println("Barber: Stopped working. Time to go home! --- " + Thread.currentThread().getName());
        }

        @Override
        public void run() {
            while (isWorking) {
                try {
                    final Integer take = seats.take();
                    System.out.println("NEXT CUSTOMER!");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    leaveWorkplace();
                    return;
                }

                try {
                    final Object take = haircutOperationPoint.take();
                    cutHair();  //Rendezvous point
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    leaveWorkplace();
                    return;
                }

                System.out.println("Haircut done!");
            }

            leaveWorkplace();
        }

        public void stopWorking() {
            isWorking = !isWorking;
        }
    }


    public static class Customer implements Runnable {
        private SynchronousQueue<Object> haircutOperationPoint;
        private ArrayBlockingQueue<Integer> seats;

        public Customer(final SynchronousQueue<Object> haircutOperationPoint, ArrayBlockingQueue<Integer> seats) {
            this.haircutOperationPoint = haircutOperationPoint;
            this.seats = seats;
        }

        public void enter() {
            System.out.println("Customer: Enters the shop --- " + Thread.currentThread().getName());
        }

        public void takeSeat() {
            System.out.println("Customer: Takes the free seat --- " + Thread.currentThread().getName());
        }

        public void getHairCut() {
            System.out.println("Customer: Getting Haircut --- " + Thread.currentThread().getName());
        }

        public void leave() {
            System.out.println("Customer: Leaves the shop --- " + Thread.currentThread().getName());
        }

        public void leaveUnconditionally() {
            System.out.println("Customer: Leaves the shop because barber shop had stopped working --- " + Thread.currentThread().getName());
        }

        @Override
        public void run() {
            enter();

            final boolean isSeatAvailable = seats.offer(1);
            if (isSeatAvailable) {
                takeSeat();
            } else {
                leave();
                return;
            }

            try {
                haircutOperationPoint.put(new Object());
                getHairCut();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                leaveUnconditionally();
            }

        }
    }

    /**
     * my solution
     */
    public static void main(String[] args) throws InterruptedException {
        final Barbershop barbershop = new Barbershop();
        barbershop.startShop();

        barbershop.customerArrive();
        barbershop.customerArrive();
        barbershop.customerArrive();
        barbershop.customerArrive();
        barbershop.customerArrive();
        barbershop.customerArrive();
        barbershop.customerArrive();
        barbershop.customerArrive();
        barbershop.customerArrive();

        Thread.sleep(5000);
        barbershop.closeShop();
    }
}
