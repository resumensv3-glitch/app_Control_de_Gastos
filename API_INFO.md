# Información de la API Integrada

## API de Tipo de Cambio

### ExchangeRate-API
- **URL Base**: `https://api.exchangerate-api.com/v6/`
- **Documentación**: https://www.exchangerate-api.com/docs/free
- **Tipo**: Gratuita (sin API key requerida para uso básico)
- **Propósito**: Obtener tasas de cambio en tiempo real

### Características:
- ✅ Gratuita
- ✅ No requiere API key
- ✅ Tasas de cambio en tiempo real
- ✅ Soporte para múltiples monedas (USD, EUR, MXN, etc.)

### Uso en la Aplicación:
La API se utiliza para mostrar tasas de cambio en la aplicación. El `ExchangeRateViewModel` carga automáticamente las tasas al iniciar y proporciona conversiones de USD a EUR y USD a MXN.

### Nota:
Si la API no está disponible, la aplicación usa valores aproximados como fallback para mantener la funcionalidad.



