package co.com.test.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {


    FRANCHISE_DOES_NOT_EXIST("FR-01", "La franquicia no existe."),
    FRANCHISE_ALREADY_EXISTS("AC-02", "La franquicia ya existe."),
    FRANCHISE_CREATION_FAILED("AC-03", "Error al crear la franquicia."),
    FRANCHISE_GET_FAILED("AC-04", "Error al consultar la franquicia."),
    FRANCHISE_UPDATE_FAILED("AC-05", "Error al actualizar la franquicia."),
    FRANCHISE_ALREADY_EXISTS_WITH_THIS_NAME("AC-06", "Error no puede actualizar la franquicia, porque ya existe una con este nombre."),


    BRANCH_ALREADY_EXISTS("BR-01", "La sucursal ya existe para esta franquicia."),
    BRANCH_CREATION_FAILED("BR-02", "Error al crear la sucursal."),
    BRANCH_DOES_NOT_EXIST("BR-03", "La sucursal no existe."),
    BRANCH_GET_FAILED("BR-04", "Error al consultar la sucursal."),
    BRANCH_ALREADY_EXISTS_WITH_THIS_NAME("BR-05", "Error no puede actualizar la sucursal, porque ya existe una con este nombre."),

    PRODUCT_DOES_NOT_EXIST("PR-01", "El producto no existe."),
    PRODUCT_ALREADY_EXISTS("PR-02", "El producto ya existe para esta sucursal."),
    PRODUCT_CREATION_FAILED("PR-03", "Error al crear el producto."),
    PRODUCT_UPDATE_STOCK_FAILED("PR-04", "Error al actualizar el stock del producto."),
    PRODUCT_DELETE_FAILED("PR-05", "Error al eliminar el producto."),
    PRODUCT_GET_TOP_FAILED("PR-06", "Error al buscar top de producto."),
    PRODUCT_GET_FAILED("PR-07", "Error al buscar el producto."),

    INVALID_INPUT("VAL-01", "Los datos de entrada no son válidos."),
    INTERNAL_ERROR("VAL-02", "Error interno, vuelva a intentarlo."),
    MISSING_REQUIRED_FIELDS("VAL-03", "Faltan campos obligatorios.");

    private final String code;
    private final String message;
}
