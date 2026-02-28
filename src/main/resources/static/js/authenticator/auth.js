async function submitForm() {
    clearErrors();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const email = document.getElementById('email').value;

    const isRegistration = document.getElementById('modeToggle').checked;

    const url = isRegistration ? '/api/auth/register' : '/login';

    const data = isRegistration
        ? JSON.stringify({username, email, password, confirmPassword})
        : `username=${username}&password=${password}`;

    const headers = isRegistration
        ? {'Content-Type': 'application/json'}
        : {'Content-Type': 'application/x-www-form-urlencoded'};

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: headers,
            body: data
        });

        if (response.ok) {
            alert(isRegistration ? "登録完了！" : "ログインしました!");
            if (!isRegistration) {
                window.location.href = "/chat.html";
            }
        } else {
            if (response.status === 401) {
                alert("ログイン失敗:ユーザー名またはパスワードが間違っています");
            } else {
                const errorData = await response.json();

                showError(errorData.field, errorData.message);
            }
        }
    } catch (error) {
        console.error("エラーの内容:", error);
    }
}