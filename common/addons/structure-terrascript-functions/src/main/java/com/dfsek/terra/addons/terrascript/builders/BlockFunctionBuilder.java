package com.dfsek.terra.addons.terrascript.builders;

import com.dfsek.terra.addons.terrascript.api.FunctionBuilder;
import com.dfsek.terra.addons.terrascript.api.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.exception.ParseException;
import com.dfsek.terra.addons.terrascript.api.lang.ConstantExpression;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.addons.terrascript.functions.BlockFunction;
import com.dfsek.terra.api.TerraPlugin;

import java.util.List;
import java.util.Map;

public class BlockFunctionBuilder implements FunctionBuilder<BlockFunction> {
    private final TerraPlugin main;

    public BlockFunctionBuilder(TerraPlugin main) {
        this.main = main;
    }

    @SuppressWarnings("unchecked")
    @Override
    public BlockFunction build(List<Returnable<?>> argumentList, Position position) throws ParseException {
        if(argumentList.size() < 4) throw new ParseException("Expected data", position);
        Returnable<Boolean> booleanReturnable = new Returnable<Boolean>() {
            @Override
            public ReturnType returnType() {
                return ReturnType.BOOLEAN;
            }

            @Override
            public Boolean apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
                return true;
            }

            @Override
            public Position getPosition() {
                return position;
            }
        };
        if(argumentList.size() == 5) booleanReturnable = (Returnable<Boolean>) argumentList.get(4);
        if(argumentList.get(3).returnType() == Returnable.ReturnType.STRING && argumentList.get(3) instanceof ConstantExpression) {
            return new BlockFunction.Constant((Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1), (Returnable<Number>) argumentList.get(2), (ConstantExpression<String>) argumentList.get(3), booleanReturnable, main, position);
        }
        return new BlockFunction((Returnable<Number>) argumentList.get(0), (Returnable<Number>) argumentList.get(1), (Returnable<Number>) argumentList.get(2), (Returnable<String>) argumentList.get(3), booleanReturnable, main, position);
    }

    @Override
    public int argNumber() {
        return -1;
    }

    @Override
    public Returnable.ReturnType getArgument(int position) {
        switch(position) {
            case 0:
            case 1:
            case 2:
                return Returnable.ReturnType.NUMBER;
            case 3:
                return Returnable.ReturnType.STRING;
            case 4:
                return Returnable.ReturnType.BOOLEAN;
            default:
                return null;
        }
    }
}