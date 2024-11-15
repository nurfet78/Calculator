import { Kind } from './kinds.js';

export class TokenStream {
    constructor(input) {
        this.input = input;
        this.pos = 0;
        this.current = { kind: Kind.end, value: 0 };
    }

    get() {
        // Пропускаем пробелы
        while (this.pos < this.input.length && /\s/.test(this.input[this.pos])) {
            this.pos++;
        }

        if (this.pos >= this.input.length) {
            return this.current = { kind: Kind.end, value: 0 };
        }

        const ch = this.input[this.pos];
        this.pos++;

        switch (ch) {
            case '^':
            case '*':
            case '/':
            case '+':
            case '-':
            case '(':
            case ')':
            case ';':
            case '=':
                return this.current = { kind: ch, value: 0 };
        }

        // Проверяем числа
        if (/[\d.]/.test(ch)) {
            let numStr = ch;
            while (this.pos < this.input.length && /[\d.]/.test(this.input[this.pos])) {
                numStr += this.input[this.pos];
                this.pos++;
            }
            return this.current = { kind: Kind.number, value: parseFloat(numStr) };
        }

        // Проверяем имена переменных
        if (/[a-zA-Z]/.test(ch)) {
            let name = ch;
            while (this.pos < this.input.length && /[a-zA-Z0-9]/.test(this.input[this.pos])) {
                name += this.input[this.pos];
                this.pos++;
            }
            if (name === 'exit') {
                return this.current = { kind: Kind.end, value: 0 };
            }
            return this.current = { kind: Kind.name, value: name };
        }

        throw new Error("Недопустимый символ: " + ch);
    }
}