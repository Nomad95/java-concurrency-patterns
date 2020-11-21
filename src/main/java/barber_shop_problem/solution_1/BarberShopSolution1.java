package barber_shop_problem.solution_1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BarberShopSolution1 {

    public static class Barbershop {
        private final int limit;
        public int customers;
        private final Barber barber;

        private final Semaphore customerReady;
        private final Semaphore barberReady;

        private final Semaphore customerDone;
        private final Semaphore barberDone;

        private final ReentrantLock mutex;

        public Barbershop(final int limit) {
            this.limit = limit;

            barber = new Barber();

            customerReady = new Semaphore(0);
            barberReady = new Semaphore(0);
            customerDone = new Semaphore(0);
            barberDone = new Semaphore(0);

            mutex = new ReentrantLock();
        }

        public Void startBarberService() {
            try {
                customerReady.acquire(); // -> wait for 1
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }

            barberReady.release(); // ->1

            barber.cutHair();  //Rendezvous point

            try {
                customerDone.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }

            barberDone.release(); //signal the customer that barber is done
            System.out.println("Haircut done!");

            return null;
        }

        public Void receiveNewCustomer() {
            final Customer customer = new Customer();

            customer.enter();

            mutex.lock();
            if (customers == limit) {
                mutex.unlock();
                customer.leave();
                return null;
            }
            //take seat
            customers += 1;
            mutex.unlock();

            customerReady.release();

            //wait for barber
            try {
                barberReady.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            customer.getHairCut(); //rendezvous point

            customerDone.release(); //inform customer is done

            try {
                barberDone.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mutex.lock();
            customers -= 1;
            mutex.unlock();

            return null;
        }
    }

    public static class Barber {

        public void cutHair() {
            System.out.println("Barber: Cutting Hair --- " + Thread.currentThread().getName());
        }
    }


    public static class Customer {

        public void enter() {
            System.out.println("Customer: Enters the shop --- " + Thread.currentThread().getName());
        }

        public void getHairCut() {
            System.out.println("Customer: Getting Haircut --- " + Thread.currentThread().getName());
        }

        public void leave() {
            System.out.println("Customer: Leaves the shop --- " + Thread.currentThread().getName());
        }
    }

    /**
     * solution From internet and its  shitty xD
     */
    public static void main(String[] args) throws InterruptedException {
        final ExecutorService executorService = Executors.newCachedThreadPool();

        final Barbershop barbershop = new Barbershop(3);

        List <Future<Void>> barberFutures = new ArrayList<>();
        List <Future<Void>> customerFutures = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            barberFutures.add(executorService.submit(barbershop::receiveNewCustomer));
            customerFutures.add(executorService.submit(barbershop::startBarberService));
        }

        barberFutures.forEach(e -> {
            try {
                e.get();
            } catch (Exception interruptedException) {
                interruptedException.printStackTrace();
            }
        });

        customerFutures.forEach(e -> {
            try {
                e.get();
            } catch (Exception interruptedException) {
                interruptedException.printStackTrace();
            }
        });

        Thread.sleep(5000);
        executorService.shutdown();
    }
}
