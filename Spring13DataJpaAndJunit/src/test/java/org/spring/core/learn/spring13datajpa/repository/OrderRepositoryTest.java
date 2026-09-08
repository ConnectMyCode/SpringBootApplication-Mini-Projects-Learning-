package org.spring.core.learn.spring13datajpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.spring.core.learn.spring13datajpa.entity.Customer;
import org.spring.core.learn.spring13datajpa.entity.Order;
import org.spring.core.learn.spring13datajpa.entity.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * CONCEPT: @DataJpaTest -> unlike @WebMvcTest, this boots the JPA/Hibernate
 * layer for real, against an actual (in-memory H2) database. It is also
 * @Transactional by default, and rolls back everything after each test - so
 * tests never leak data into one another.
 *
 * This is the ONLY layer where mocking the repository would be pointless -
 * we WANT to prove the real @Query/@NamedQuery/method-name query actually
 * produces correct SQL/JPQL against a real schema.
 */

/**
* 2. @Transactional being true by default — yes, and here's exactly what that buys you

@DataJpaTest is meta-annotated with @Transactional. That means:

Spring opens a transaction before @BeforeEach runs
Your @BeforeEach setup and the @Test method both execute inside that same transaction
After the test method finishes, Spring rolls the transaction back — none of the
* data you saved (testCustomer, the 4 orders) actually stays in the database
* */



@DataJpaTest
public class OrderRepositoryTest {

@Autowired
    private OrderRepository orderRepository;

@Autowired
    private CustomerRepository customerRepository;


private Customer testCustomer;
private List<Order> orderSummary;

    @BeforeEach
    void setUp() {
        //ARRANGE
        testCustomer = new Customer(null, "Ravi Kumar", "ravi@example.com", new ArrayList<>());
        customerRepository.save(testCustomer);   // parent must exist before children reference it

        orderSummary = List.of(
                new Order(null, 1000.0, OrderStatus.DELIVERED, LocalDate.now(), testCustomer.getId()),
                new Order(null, 500.0,  OrderStatus.DELIVERED, LocalDate.now(), testCustomer.getId()),
                new Order(null, 400.0,  OrderStatus.DELIVERED, LocalDate.now(), testCustomer.getId()),
                new Order(null, 4000.0, OrderStatus.PENDING,   LocalDate.now(), testCustomer.getId())
        );
        orderRepository.saveAll(orderSummary);   // <-- you were missing this: the customer got saved, but the orders never did
    }

//************************************************************************************************************************************************

@Test
    @DisplayName("Approach 1: findByStatus() method-name query returns matching rows")
void findByStatus_returnsMatchingOrders(){

    //Arrange : @BeforeEach method handled it...

    //ACT
    List<Order> orderListDelievered = orderRepository.findByStatus(OrderStatus.DELIVERED);
    List<Order> orderListShipped = orderRepository.findByStatus(OrderStatus.SHIPPED);

    //ASSERT
    assertThat(orderListDelievered).hasSize(3);
    assertThat(orderListShipped).isEmpty();        // assertEquals(0, orderListShipped.size());
    assertThat(orderListDelievered).extracting(Order::getAmount).contains(1000.0, 500.0, 400.0);

}

//************************************************************************************************************************************************

@Test
@DisplayName("Approach2: ByMethodName query returns orders Whose Amount is greater than Given Amount Parameter:Amount")
void findByAmountGreaterThan()
{

    Double amount = 500D;
    //ACT
    List<Order> amountList =  orderRepository.findByAmountGreaterThan(amount);

    Long order1Id = orderSummary.get(0).getId(); // amount 1000.0
    Long order4Id = orderSummary.get(3).getId(); // amount 4000.0

    // Assert
    assertThat(amountList.get(1).equals(testCustomer));
    assertThat(amountList).extracting(Order::getId).contains(order1Id , order4Id); //Verifying if Order In the list having Amount >500 are the Correcter Orders Via IDs as Id: 4l and 1L -> Amount: 1000 ,4000...
}

//************************************************************************************************************************************************

//Below Tests are Testing 2 Methods of OrderRepository
    /*
        1.Query Annotation
         @Query(value = "select o from Order o where o.amount > :amount and o.status = :status")
        List<Order> fetchOrdersByAmountAndStatus(@Param("amount") Double amount, @Param("status") OrderStatus status);

        2.NamedQuey   -->Actual JPQL query is written inside the Entity class...
         List<Order> fetchByAmountAndStatus(@Param("amount") Double amount, @Param("status") OrderStatus status);

        Both these methods do the same thing Based on Amount and Status they return List<Order> of A Customer...
         FOCUS ON : How the queries are fired differenctly in backend
     */
@Test
@DisplayName("Approach 3: @Query JPQL correctly filters by amount AND status")
void fetchOrdersByAmountAndStatus_appliesBothConditions() {

    List<Order> result = orderRepository.fetchOrdersByAmountAndStatus(500.0, OrderStatus.DELIVERED);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getAmount()).isEqualTo(500.0);
}



@Test
@DisplayName("Approach 4: @NamedQuery defined on the entity resolves and executes correctly")
void fetchByAmountAndStatus_usesNamedQuery() {

    List<Order> result = orderRepository.fetchByAmountAndStatus(1000.0, OrderStatus.SHIPPED);

    assertThat(result).hasSize(1);

}

//************************************************************************************************************************************************
@Test
@DisplayName("Approach5: Bidirectional relationship: FK column is actually persisted (not null)")
void addOrder_persistsForeignKeyCorrectly() {

    // This test directly guards against the pitfall discussed earlier:
    // forgetting to set the back-reference and getting a NULL customer_id.
    //In simple words it means that as per this relation betn Customer And Order One To Many it is a Bidirectional relationship
    //Becuase while creating both entity notice that we have   'CustomerId'-->ForeignKey inside Order Entity and OrderId inside Customer Entity with annotation @JoinColum and @OneToMany
    //What it means : When A Order is created and we forgot to add the Customer id in parameter it will cas



    //ACT
    List<Order> ordersForCustomer = orderRepository.findByCustomerId(testCustomer.getId());


    //ASSERT
    assertThat(ordersForCustomer).hasSize(4);
    assertThat(ordersForCustomer.get(0).getCustomerId()).isEqualTo(testCustomer.getId());


    /*
    *
            * Yes — **that is essentially the purpose of this test**, with one important refinement.

        The test is verifying that the **relationship between `Order` and `Customer` is actually persisted in the database**, not merely present in the Java objects.

        ### What it is checking

        Suppose:

        ```text
        Customer
        id = 1
        name = "FK Test"

                │
                │ customer_id = 1
                ↓

        Order
        id = 10
        amount = 750
        status = PENDING
        ```

        The test wants to prove:

        1. A `Customer` is created.
        2. An `Order` is created.
        3. The `Order` is associated with that `Customer`.
        4. Hibernate persists that relationship.
        5. In the **database**, the `orders.customer_id` FK actually contains `1`.
        6. When the Order is retrieved from the database, its `customer` relationship points back to Customer `1`.

        So this assertion:

        ```java
        assertThat(ordersForCustomer.get(0).getCustomer().getId())
                .isEqualTo(customer.getId());
        ```

        is effectively asking:

        > **"I saved this Order with Customer X. When I retrieve the Order from the database, does Hibernate/database still know that this Order belongs to Customer X?"**

        ### Why this test is useful

        It catches a common bidirectional JPA mistake:

        ```text
        Customer
           │
           └── orders → Order
        ```

        You might add the Order to the Customer's collection, but forget:

        ```java
        order.setCustomer(customer);
        ```

        If `Order` is the **owning side**, Hibernate takes the FK information from `Order.customer`.

        So you could end up with:

        ```text
        Java memory:

        Customer → Order
        Order → null


        Database:

        orders
        --------------------------------
        id | amount | customer_id
        10 | 750    | NULL       ❌
        ```

        The test detects exactly this kind of problem.

        ### Therefore, your understanding is correct

        > **The test verifies that when an Order is created and associated with a Customer, the Customer's ID is actually persisted as the foreign key in the database, and the relationship can be retrieved correctly afterward.**

        It is **not primarily testing whether an Order object can be created**. It is testing the **persistence of the relationship/FK**.

    * */



}





}