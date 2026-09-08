package org.spring.core.learn.spring13datajpa.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spring.core.learn.spring13datajpa.dto.CustomerOrderSummary;
import org.spring.core.learn.spring13datajpa.entity.Customer;
import org.spring.core.learn.spring13datajpa.entity.Order;
import org.spring.core.learn.spring13datajpa.entity.OrderStatus;
import org.spring.core.learn.spring13datajpa.repository.CustomerRepository;
import org.spring.core.learn.spring13datajpa.repository.OrderRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * CONCEPT: TRUE unit test - OrderService is tested completely in isolation.
 * Its two dependencies (CustomerRepository, OrderRepository) are replaced with
 * Mockito mocks, so no Spring context boots and no real database is touched.
 * This makes the test fast (milliseconds) and focused purely on OUR logic:
 * the Stream API aggregation and the multithreaded fan-out.
 *
 * @ExtendWith(MockitoExtension.class) -> JUnit5's way of plugging Mockito into
 * the test lifecycle, so @Mock and @InjectMocks fields get initialized
 * automatically before each test method runs.
 */

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest
{
    @Mock private CustomerRepository customerRepository;

    @Mock private OrderRepository orderRepository;

    // CONCEPT: @InjectMocks -> Mockito creates a real OrderService instance and
    // injects the two @Mock fields above into its constructor automatically.
    @InjectMocks private OrderService orderServices; //real objects, fake deps wired in

    private Customer testCustomer;

    // CONCEPT: @BeforeEach -> JUnit5 runs this before EVERY @Test method,
    // giving each test a clean, independent starting state.
    @BeforeEach
    void setup(){
        testCustomer = new Customer(1L, "Ravi Kumar","ravi@example.com", new ArrayList<>() );
    }

    @Test
    @DisplayName("placeOrder() should add the order to the customer and save via cascade")
    void placeOrder_addsOrderAndSavesCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

                                                                                                 //CustomerId
        Order newOrder = new Order(null, 500.0, OrderStatus.PENDING, LocalDate.now(), testCustomer.getId());

        //ACT
        Order result = orderServices.placeOrder(1L, newOrder);


        //ASSERT
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.getCustomerId()).isEqualTo(testCustomer.getId());
        assertThat(testCustomer.getOrders()).hasSize(1);

/*        //Here VErifying Number of order placed is "1 by testCustomer" and checking if this method  {buildSummaryFor} works or not by giving correct no. i..ie. 1
        CustomerOrderSummary summary = orderServices.buildSummaryFor(testCustomer);
        assertEquals(1L , summary.getTotalOrders() );
*/
        // CONCEPT: verify() -> checks a mock method was actually CALLED,
        // not just that the result looks right. Confirms our code really
        // delegated the save to the repository, exactly once.
        verify(customerRepository, times(1)).save(testCustomer);
    }



        @Test
        @DisplayName("buildSummaryFor() should correctly su amounts and group by status using streams")
        void summaryFor_CustomerOrderSummary(){


        //ARRANGE
            List<Order> orderSummary = List.of(
                  new Order(1L, 1000.0 , OrderStatus.DELIVERED, LocalDate.now(), testCustomer.getId()),
                  new Order(2L, 500.0 , OrderStatus.DELIVERED, LocalDate.now(), testCustomer.getId()),
                  new Order(3L, 400.0 , OrderStatus.DELIVERED, LocalDate.now(), testCustomer.getId()),
                  new Order(4L, 600.0 , OrderStatus.PENDING, LocalDate.now(), testCustomer.getId()),
                  new Order(5L, 100.0 , OrderStatus.SHIPPED, LocalDate.now(), testCustomer.getId()),
                  new Order(6L, 750.0 , OrderStatus.CANCELED, LocalDate.now(), testCustomer.getId())
            );



            when(orderRepository.findByCustomerId(1L)).thenReturn(orderSummary);


            //ACT
            CustomerOrderSummary summary=  orderServices.buildSummaryFor(testCustomer);


            //Assert
            //Verify Stream API total should be 2600...

            assertEquals(2600 , summary.getTotalAmountSpent() );
            assertEquals(6 , summary.getTotalOrders());



        //    buildSummaryFor() methods return type is -> CustomerOrderSummary dto -> contains field -> OrderCountByStatus of type Map<OrderStatus, Long>

            //verify  Collectors.groupingBy produced the right counts
            assertEquals(1L ,summary.getOrderCountByStatus().get(OrderStatus.SHIPPED) );
            assertEquals(3L, summary.getOrderCountByStatus().get(OrderStatus.DELIVERED) );

        }



        @Test
        @DisplayName("generateReportForAllCustomersConcurrently() should return onw summary per sutdent")

    void generateReport_returnsOneSummaryPerCustomer() {

        //ARRANGE
        Customer customer1 = new Customer(1L, "Ravi Kumar", "ravi@example.com", new ArrayList<>());
        Customer customer2  =new Customer(2L,  "Sneha Rao", "sneha@example.com", new ArrayList<>());

        List<Customer> customerList = List.of(customer1 , customer2);

        List<Order> newOrder1 = List.of(new Order(1L, 100.0, OrderStatus.DELIVERED, LocalDate.now(), customer1.getId()));
        List<Order> newOrder2 = List.of(new Order(2L, 900.0, OrderStatus.DELIVERED, LocalDate.now(), customer2.getId()));


        when(customerRepository.findAll()).thenReturn(customerList);

            // Each customer's orders are looked up independently once the thread pool
            // fans out - so we program both possible calls.


            when(orderRepository.findByCustomerId(1L)).thenReturn(newOrder1);
            when(orderRepository.findByCustomerId(2L)).thenReturn(newOrder2);


        /*
        / ACT: this internally spins up an ExecutorService, submits 2 tasks,
        // and blocks on future.get() until both complete.
               * */
            List<CustomerOrderSummary> report = orderServices.generateReportForAllCustomersConcurrently();


        //ASSERT
        // ASSERT: regardless of thread scheduling/order, we must get exactly
        // 2 results back, and the totals must be correct for each.

        assertThat(report).hasSize(2);
        assertThat(report)
                .extracting(CustomerOrderSummary::getTotalAmountSpent)
                .containsExactlyInAnyOrder(100.0, 900.0);
        }
}
