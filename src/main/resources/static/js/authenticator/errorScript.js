function showError(fieldId, message) {
    const errorSpan = document.getElementById(`error-${fieldId}`);
    const inputField = document.getElementById(fieldId);
    if (errorSpan) {
        errorSpan.innerText = message;
        message ? inputField.classList.add('input-error') : inputField.classList.remove('input-error');
    }
}

function clearErrors() {
    document.querySelectorAll('.error-text').forEach(el => el.innerText = '');
    document.querySelectorAll('input').forEach(el => el.classList.remove('input-error'));
}

document.getElementById('username').addEventListener('input', (e) => {
    const len = e.target.value.length;

    const message = (len > 0 && (len < 3 || len > 15))
        ? '3文字から15文字まで入力してください'
        : '';

    showError('username', message);
});

document.getElementById('password').addEventListener('input', (e) => {
    const len = e.target.value.length;

    const message = (len > 0 && len < 6) ? 'パスワードが短すぎます (最小6字)' : '';
    showError('password', message);

    checkPasswordsMatch();
});

document.getElementById('confirmPassword').addEventListener('input', () => {
    checkPasswordsMatch();
});

function checkPasswordsMatch() {
    const pass = document.getElementById('password').value;
    const confirm = document.getElementById('confirmPassword').value;

    if (confirm.length > 0) {
        const message = (pass !== confirm) ? 'パスワードが一致しません' : '';
        showError('confirmPassword', message);
    }
}