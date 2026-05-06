/**
 * Personal Library — JavaScript Principal
 *
 * Funcionalidades:
 * - Auto-dismiss de alertas após 5 segundos
 * - Confirmação de exclusão (redundância ao confirm() do HTML)
 * - Formatação automática de ISBN
 */

document.addEventListener('DOMContentLoaded', function () {

    // ==============================
    // Auto-dismiss de alertas
    // ==============================
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(function () {
                alert.style.display = 'none';
            }, 500);
        }, 5000); // Desaparece após 5 segundos
    });

    // ==============================
    // Formatação de ISBN
    // Adiciona hífens automaticamente ao digitar
    // ==============================
    const isbnInput = document.getElementById('isbn');
    if (isbnInput) {
        isbnInput.addEventListener('input', function () {
            // Remove tudo que não for número ou hífen
            let value = this.value.replace(/[^0-9-]/g, '');
            this.value = value;
        });
    }

    // ==============================
    // Indicador de força de senha
    // ==============================
    const passwordInput = document.getElementById('password');
    if (passwordInput && document.getElementById('confirmPassword')) {
        passwordInput.addEventListener('input', function () {
            const strength = getPasswordStrength(this.value);
            const indicator = document.getElementById('password-strength');
            if (indicator) {
                indicator.textContent = strength.label;
                indicator.className = 'password-strength strength-' + strength.level;
            }
        });
    }

    // ==============================
    // Validação de confirmação de senha no cliente
    // (O servidor também valida — defesa em profundidade)
    // ==============================
    const confirmInput = document.getElementById('confirmPassword');
    if (confirmInput && passwordInput) {
        confirmInput.addEventListener('input', function () {
            if (this.value !== passwordInput.value) {
                this.setCustomValidity('As senhas não conferem');
                this.style.borderColor = 'var(--danger)';
            } else {
                this.setCustomValidity('');
                this.style.borderColor = 'var(--success)';
            }
        });
    }
});

/**
 * Calcula a força da senha.
 * @param {string} password
 * @returns {{ level: string, label: string }}
 */
function getPasswordStrength(password) {
    if (password.length < 6) return { level: 'weak', label: 'Fraca' };
    let score = 0;
    if (password.length >= 8) score++;
    if (/[A-Z]/.test(password)) score++;
    if (/[0-9]/.test(password)) score++;
    if (/[^A-Za-z0-9]/.test(password)) score++;

    if (score >= 3) return { level: 'strong', label: 'Forte' };
    if (score >= 2) return { level: 'medium', label: 'Média' };
    return { level: 'weak', label: 'Fraca' };
}