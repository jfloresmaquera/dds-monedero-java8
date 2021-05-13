package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo;
  private List<Deposito> depositos = new ArrayList<>();
  private List<Extraccion> extracciones = new ArrayList<>();

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void depositar(double monto) {
    validarDeposito(monto);
    agregarDeposito(monto);
  }

  public void extraer(double monto) {//Large method puedo separarlo en 4 metodos
    validarExtraccion(monto);
    agregarExtraccion(monto);
  }

  public void validarDeposito(double monto){
    validarMontoNegativo(monto);
    validarDepositosDiariosNoSupera(3);
  }

  public void validarMontoNegativo(double monto){
    if(monto<0){
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void validarDepositosDiariosNoSupera(int cantidad){
    if (getDepositos().stream().count() >= cantidad) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  public void validarExtraccion(double monto){
    validarMontoNegativo(monto);
    validarExtraccionConSaldoSuficiente(monto);
    validarMaximoMontoRetirableDiario(monto);
  }

  public void validarExtraccionConSaldoSuficiente(double monto){
    if (getSaldo() - monto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  public void validarMaximoMontoRetirableDiario(double monto){
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (monto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
              + " diarios, límite: " + limite);
    }
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
