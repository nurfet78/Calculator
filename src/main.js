import { Calculator } from './calculator.js';

const calculator = new Calculator();

export function calculate() {
    const input = document.getElementById('expression').value;
    const resultDiv = document.getElementById('result');

    try {
        const results = calculator.evaluateMultiple(input);

        // Очищаем предыдущие результаты
        resultDiv.innerHTML = '';

        // Добавляем новые результаты
        results.forEach(result => {
            const resultItem = document.createElement('div');
            resultItem.className = 'alert alert-success mb-2';
            resultItem.textContent = `${result.expression} = ${result.value.toFixed(3)}`;
            resultDiv.appendChild(resultItem);
        });
    } catch (error) {
        resultDiv.innerHTML = '';
        const errorDiv = document.createElement('div');
        errorDiv.className = 'alert alert-danger';
        errorDiv.textContent = `Ошибка: ${error.message}`;
        resultDiv.appendChild(errorDiv);
    }
}