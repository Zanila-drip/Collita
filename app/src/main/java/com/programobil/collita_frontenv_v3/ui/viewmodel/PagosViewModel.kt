package com.programobil.collita_frontenv_v3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programobil.collita_frontenv_v3.data.api.PagoDto
import com.programobil.collita_frontenv_v3.data.api.UserResponse
import com.programobil.collita_frontenv_v3.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class PagosState {
    object Loading : PagosState()
    data class Success(val pagos: List<PagoDto>) : PagosState()
    data class Error(val message: String) : PagosState()
    object Empty : PagosState()
}

class PagosViewModel : ViewModel() {
    private val _state = MutableStateFlow<PagosState>(PagosState.Empty)
    val state: StateFlow<PagosState> = _state

    fun cargarPagosPendientesMes() {
        _state.value = PagosState.Loading
        viewModelScope.launch {
            try {
                val pagos = RetrofitClient.pagoService.getPagos()
                println("TODOS LOS PAGOS RECIBIDOS: $pagos")

                val pagosPendientes = pagos.filter {
                    it.estado == "pendiente"
                }
                println("PAGOS PENDIENTES: $pagosPendientes")

                if (pagosPendientes.isEmpty()) {
                    _state.value = PagosState.Empty
                } else {
                    _state.value = PagosState.Success(pagosPendientes)
                }
            } catch (e: Exception) {
                _state.value = PagosState.Error("Error al cargar pagos: "+e.localizedMessage)
            }
        }
    }

    fun marcarComoPagado(pago: PagoDto, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val actualizado = pago.copy(estado = "pagado")
                RetrofitClient.pagoService.actualizarPago(pago.id!!, actualizado)
                cargarPagosPendientesMes()
                onSuccess()
            } catch (e: Exception) {
                // Manejo de error opcional
            }
        }
    }
} 