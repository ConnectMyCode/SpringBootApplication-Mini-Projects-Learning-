package org.spring.core.learn.spring13datajpa.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.spring.core.learn.spring13datajpa.dto.CustomerOrderSummary;
import org.spring.core.learn.spring13datajpa.entity.Order;
import org.spring.core.learn.spring13datajpa.entity.OrderStatus;
import org.spring.core.learn.spring13datajpa.repository.OrderRepository;
import org.spring.core.learn.spring13datajpa.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CustomerController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Mock
    private OrderRepository orderRepository;


    @Mock
    private OrderService orderService;


    @Autowired
    private ObjectMapper objectMappers;

    @Test
    @DisplayName("POST /api/orders/customer/{id} -> delegates to OrderService.placeOrder")
    void placeOrder_delegatesToService() throws Exception {

        Order requestBody = new Order(null, 500.0, OrderStatus.PENDING, LocalDate.now(), null);
        Order saveOrder = new Order(10L, 500.0, OrderStatus.PENDING, LocalDate.now(), null);

        // CONCEPT: anyLong() / any() -> Mockito "argument matchers". We don't
        // care about the EXACT object passed in for this assertion, just that
        // SOME Long and SOME Order were passed - so we stub loosely here.


        when(orderService.placeOrder(anyLong(), any(Order.class))).thenReturn(saveOrder);


        mockMvc.perform(post("/api/orders/customer/{customerId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMappers.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.status").value(OrderStatus.PENDING));


        verify(orderService, times(1)).placeOrder(eq(1L), any(Order.class));

    }


    @Test
    @DisplayName("GET /api/orders/status/{status} -> method-name query (Approach 1)")
    void getByStatus_delegatesToRepository() throws Exception {
        Order o1 = new Order(1L, 1000.0, OrderStatus.DELIVERED, LocalDate.now(), null);

        when(orderRepository.findByStatus(OrderStatus.DELIVERED)).thenReturn(List.of(o1));


        //Input Given : Order Object --> Fields: LocalDate orderDate;  || OrderStatus status; || Double amount; || Long id;
        //endpoint :/status/{status} RequestBody  :
        //Input Required:public List<Order> getByStatus(@PathVariable OrderStatus status)
        //Returns:List<Order>
        mockMvc.perform(get("/api/orders/status/{status}", OrderStatus.DELIVERED)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(1000.0));
        //List Index : 0 :First Elemen of List...

    /*
    *                     REQUEST
                       │
                       ▼
        GET /api/orders/status/DELIVERED
                       │
                       │
                 URL Path Variable
                       │
                       ▼
        @PathVariable OrderStatus status
                       │
                       ▼
              OrderStatus.DELIVERED
                       │
                       ▼
             Controller method
                       │
                       ▼
       repository.findByStatus(DELIVERED)
                       │
                       ▼
                  List<Order>
                       │
                       ▼
              JSON Response
                       │
                       ▼
[
  {
    "id": 1,
    "amount": 1000.0,
    "status": "DELIVERED",
    "orderDate": "..."
  }
]
    *
    * */

    }

    @Test
    @DisplayName("GET /api/orders/filter -> @Query JPQL method (Approach 2)")
    void filterOrders_delegatesToJpqlQuery() throws Exception {
        Order o1 = new Order(1L, 1000.0, OrderStatus.DELIVERED, LocalDate.now(), null);
        when(orderRepository.fetchOrdersByAmountAndStatus(500.0, OrderStatus.DELIVERED))
                .thenReturn(List.of(o1));

        mockMvc.perform(get("api/orders/filter")
                        .param("amount", "500.0")
                        .param("status", "DELIVERED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

    }


    @Test
    @DisplayName("GET /api/orders/named-query -> @NamedQuery method (Approach 3)")
    void namedQueryDemo_delegatesToNamedQuery() throws Exception {
        Order o1 = new Order(1L, 1000.0, OrderStatus.DELIVERED, LocalDate.now(), null);
        when(orderRepository.fetchByAmountAndStatus(500.0, OrderStatus.DELIVERED))
                .thenReturn(List.of(o1));

        mockMvc.perform(
                        get("/api/orders/named-query")
                                .param("amount", "500.0")
                                .param("status", "DELIVERED")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(OrderStatus.DELIVERED));
    }


    @Test
    @DisplayName("GET /api/orders/reports/summary -> returns the concurrently-built report")
    void getFullReport_returnsSummaryList() throws Exception {

        //ARRANGE
        CustomerOrderSummary summary = new CustomerOrderSummary(
                1L, "Ravi Kumar", 2, 1500.0, Map.of(OrderStatus.DELIVERED, 2L)
        );
        when(orderService.generateReportForAllCustomersConcurrently())
                .thenReturn(List.of(summary));


        //ACT
        mockMvc.perform(get("/api/orders/reports/summary"))
                /*ASSER*/.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("Ravi Kumar"))
                .andExpect(jsonPath("$[0].totalAmountSpent").value(1500.0));


    }
}





