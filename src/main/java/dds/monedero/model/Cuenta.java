package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Deposito> depositos = new ArrayList<>();
  private List<Extraccion> extracciones = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }//Ver porque doble incializacion de monto

  public void poner(double cuanto) { //Large method, se puede separar en 3 methods
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }

    if (getDepositos().stream().count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

    agregarDeposito(cuanto);
  }

  public void sacar(double cuanto) {//Large method puedo separarlo en 4 metodos
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }//repeticion logica
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    agregarExtraccion(cuanto);
  }

  public void agregarDeposito(double monto){
    Deposito unDeposito = new Deposito(LocalDate.now(),monto);
    getDepositos().add(unDeposito);
    saldo+=monto;
  }

  public void agregarExtraccion(double monto){
    Extraccion unaExtraccion = new Extraccion(LocalDate.now(),monto);
    getExtracciones().add(unaExtraccion);
    saldo-=monto;
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getExtracciones().stream()
        .filter(movimiento -> movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Deposito> getDepositos() {
    return depositos;
  }

  public List<Extraccion> getExtracciones() {
    return extracciones;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }//Ver porquepodes setear tu saldo en cualquier momento

}
