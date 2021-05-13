package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta(0);
  }

  @Test
  void RealizarDepositosCorrectamente() {
    cuenta.depositar(1800);
    cuenta.depositar(1500);
    assertEquals(3300,cuenta.getSaldo());
  }

  @Test
  void RealizarExtraccionesCorrectamente() {
    cuenta.setSaldo(10000);
    cuenta.extraer(300);
    cuenta.extraer(450);
    assertEquals(9250,cuenta.getSaldo());
  }

  @Test
  void NoSePuedeRealizarDepositoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.depositar(-1500));
  }

  @Test
  void NoSePuedeRealizarMasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.depositar(1500);
          cuenta.depositar(456);
          cuenta.depositar(1900);
          cuenta.depositar(245);
    });
  }

  @Test
  public void NoSePuedeRealizarExtraccionConMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.extraer(-500));
  }

  @Test
  void NoSePuedeRealizarExtraccionMayorQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.extraer(100);
    });
  }

  @Test
  public void NoSePuedeRealizarExtraccionMayorA1000EnUnDia() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.extraer(300);
      cuenta.extraer(600);
      cuenta.extraer(500);
    });
  }

}