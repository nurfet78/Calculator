import {Kind} from './kinds.js';
import {TokenStream} from './token-stream.js';

export class Calculator {
    constructor() {
        this.variables = new Map([
            ['pi', Math.PI],
            ['e', Math.E]
        ]);
    }

    prim(ts, get) {
        if (get) ts.get();

        switch (ts.current.kind) {
            case Kind.number:
                const value = ts.current.value;
                ts.get();
                return value;

            case Kind.name:
                const name = ts.current.value;
                const varValue = this.variables.get(name) || 0;
                if (ts.get().kind === Kind.assign) {
                    const newValue = this.expr(ts, true);
                    this.variables.set(name, newValue);
                    return newValue;
                }
                return varValue;

            case Kind.minus:
                return -this.prim(ts, true);

            case Kind.lp:
                const result = this.expr(ts, true);
                if (ts.current.kind !== Kind.rp) {
                    throw new Error("Ожидается ')'");
                }
                ts.get();
                return result;

            default:
                throw new Error("Ожидается число или '('");
        }
    }

    degree(ts, get) {
        let left = this.prim(ts, get);

        while (ts.current.kind === Kind.deg) {
            ts.get();
            left = Math.pow(left, this.prim(ts, false));
        }

        return left;
    }

    term(ts, get) {
        let left = this.degree(ts, get);

        for (;;) {
            if (ts.current.kind === Kind.mul) {
                ts.get();
                left *= this.degree(ts, false);
            }
            else if (ts.current.kind === Kind.div) {
                ts.get();
                const divisor = this.degree(ts, false);
                if (divisor === 0) throw new Error("Деление на ноль");
                left /= divisor;
            }
            else break;
        }

        return left;
    }

    expr(ts, get) {
        let left = this.term(ts, get);

        for (;;) {
            if (ts.current.kind === Kind.plus) {
                ts.get();
                left += this.term(ts, false);
            }
            else if (ts.current.kind === Kind.minus) {
                ts.get();
                left -= this.term(ts, false);
            }
            else break;
        }

        return left;
    }

    evaluateMultiple(input) {
        const expressions = input.split('\n').filter(expr => expr.trim());
        const results = [];

        for (const expr of expressions) {
            const ts = new TokenStream(expr.trim());
            ts.get();
            const result = this.expr(ts, false);

            results.push({
                expression: expr.trim(),
                value: result
            });
        }

        return results;
    }
}