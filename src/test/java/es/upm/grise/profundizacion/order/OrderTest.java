package es.upm.grise.profundizacion.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.upm.grise.exceptions.IncorrectItemException;

@ExtendWith(MockitoExtension.class)
public class OrderTest {

    private Order order;
    
    @Mock
    private Item item1;
    
    @Mock
    private Item item2;
    
    @Mock
    private Product product;

    @BeforeEach
    public void setUp() {
        // Inicializamos una orden vacia antes de cada prueba
        order = new Order();
    }

    @Test
    public void testOrderItemsCollectionIsInitiallyEmpty() {
        // Verificamos que la lista se inicializa vacia pero no nula
        assertNotNull(order.getItems(), "La lista de items no deberia ser nula");
        assertTrue(order.getItems().isEmpty(), "La lista de items deberia estar vacia al inicializar");
    }

    @Test
    public void testAddItemThrowsExceptionWhenPriceIsNegative() {
        // Configuramos el mock para devolver un precio negativo
        when(item1.getPrice()).thenReturn(-1.0);

        // Verificamos que lanza la excepcion
        assertThrows(IncorrectItemException.class, () -> {
            order.addItem(item1);
        }, "Deberia lanzar IncorrectItemException si el precio es negativo");
    }

    @Test
    public void testAddItemThrowsExceptionWhenQuantityIsZero() throws IncorrectItemException {
        // Configuramos precio valido pero cantidad cero
        when(item1.getPrice()).thenReturn(10.0);
        when(item1.getQuantity()).thenReturn(0);

        // Verificamos que lanza la excepcion
        assertThrows(IncorrectItemException.class, () -> {
            order.addItem(item1);
        }, "Deberia lanzar IncorrectItemException si la cantidad es cero");
    }

    @Test
    public void testAddItemThrowsExceptionWhenQuantityIsNegative() throws IncorrectItemException {
        // Configuramos precio valido pero cantidad negativa
        when(item1.getPrice()).thenReturn(10.0);
        when(item1.getQuantity()).thenReturn(-5);

        // Verificamos que lanza la excepcion
        assertThrows(IncorrectItemException.class, () -> {
            order.addItem(item1);
        }, "Deberia lanzar IncorrectItemException si la cantidad es negativa");
    }

    @Test
    public void testAddItemAddsNewItemWhenListIsEmpty() throws IncorrectItemException {
        // Configuramos un item valido
        when(item1.getPrice()).thenReturn(10.0);
        when(item1.getQuantity()).thenReturn(1);
        
        // Ejecucion
        order.addItem(item1);

        // Verificacion
        assertEquals(1, order.getItems().size(), "El item deberia haberse anadido a la lista");
        assertTrue(order.getItems().contains(item1), "La lista deberia contener el item especifico");
    }

    @Test
    public void testAddItemIncrementsQuantityWhenProductAndPriceAreSame() throws IncorrectItemException {
        // Escenario: El mismo producto con el mismo precio se anade dos veces.
        // Se espera que no se anada un nuevo elemento, sino que se sume la cantidad al existente.

        // Configuracion Item 1 (Existente)
        when(item1.getProduct()).thenReturn(product);
        when(item1.getPrice()).thenReturn(10.0);
        when(item1.getQuantity()).thenReturn(5);

        // Configuracion Item 2 (Nuevo intento de adicion)
        when(item2.getProduct()).thenReturn(product); // Mismo objeto producto
        when(item2.getPrice()).thenReturn(10.0);      // Mismo precio
        when(item2.getQuantity()).thenReturn(3);

        // Ejecucion
        order.addItem(item1);
        order.addItem(item2);

        // Verificaciones
        // 1. La lista sigue teniendo solo 1 elemento
        assertEquals(1, order.getItems().size(), "No se deberia anadir un nuevo elemento a la lista");
        
        // 2. Se debe haber llamado a setQuantity en el item1 con la suma (5 + 3 = 8)
        // Nota: Order.java hace: i.setQuantity(i.getQuantity() + item.getQuantity());
        verify(item1).setQuantity(8);
    }

    @Test
    public void testAddItemAddsNewEntryWhenProductIsSameButPriceIsDifferent() throws IncorrectItemException {
        // Escenario: El mismo producto pero con diferente precio (diferente proveedor).
        // Se espera que se anada como un item separado.

        // Configuracion Item 1
        when(item1.getProduct()).thenReturn(product);
        when(item1.getPrice()).thenReturn(10.0);
        when(item1.getQuantity()).thenReturn(1);

        // Configuracion Item 2 (Mismo producto, precio distinto)
        when(item2.getProduct()).thenReturn(product);
        when(item2.getPrice()).thenReturn(12.5); 
        when(item2.getQuantity()).thenReturn(1);

        // Ejecucion
        order.addItem(item1);
        order.addItem(item2);

        // Verificaciones
        assertEquals(2, order.getItems().size(), "Se deberian tener dos elementos distintos debido a la diferencia de precio");
    }

    @Test
    public void testAddItemAddsNewEntryWhenProductIsDifferent() throws IncorrectItemException {
        Product productB = mock(Product.class);

        // Configuracion Item 1
        when(item1.getProduct()).thenReturn(product);
        when(item1.getPrice()).thenReturn(10.0);
        when(item1.getQuantity()).thenReturn(1);

        // Configuracion Item 2 (Producto distinto)
        when(item2.getProduct()).thenReturn(productB);
        when(item2.getPrice()).thenReturn(10.0);
        when(item2.getQuantity()).thenReturn(1);

        // Ejecucion
        order.addItem(item1);
        order.addItem(item2);

        // Verificaciones
        assertEquals(2, order.getItems().size(), "Se deberian tener dos elementos al ser productos distintos");
    }
}
