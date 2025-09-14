// Dados simulados de um único cliente
let clienteAtual = {
    name: 'João Silva',
    email: 'joao.silva@email.com',
    cpf: '123.456.789-00',
    phone: '(11) 98765-4321'
};

// Referências aos elementos HTML
const profileContainer = document.getElementById('client-profile-container');
const clientNameSpan = document.getElementById('client-name');
const clientEmailSpan = document.getElementById('client-email');
const clientCpfSpan = document.getElementById('client-cpf');
const clientPhoneSpan = document.getElementById('client-phone');
const editBtn = document.getElementById('edit-btn');
const deleteBtn = document.getElementById('delete-btn');

const editForm = document.getElementById('edit-form');
const editNameInput = document.getElementById('edit-name');
const editEmailInput = document.getElementById('edit-email');
const editCpfInput = document.getElementById('edit-cpf');
const editPhoneInput = document.getElementById('edit-phone');
const cancelEditBtn = document.getElementById('cancel-edit-btn');

// Função para exibir os dados do cliente na página
function displayClient() {
    clientNameSpan.textContent = clienteAtual.name;
    clientEmailSpan.textContent = clienteAtual.email;
    clientCpfSpan.textContent = clienteAtual.cpf;
    clientPhoneSpan.textContent = clienteAtual.phone;
}

// Evento de clique para o botão Editar
editBtn.addEventListener('click', () => {
    // Preenche o formulário com os dados atuais do cliente
    editNameInput.value = clienteAtual.name;
    editEmailInput.value = clienteAtual.email;
    editCpfInput.value = clienteAtual.cpf;
    editPhoneInput.value = clienteAtual.phone;
    
    // Esconde o perfil e mostra o formulário de edição
    profileContainer.classList.add('d-none');
    editForm.classList.remove('d-none');
});

// Evento de clique para o botão Excluir
deleteBtn.addEventListener('click', () => {
    if (confirm('Tem certeza que deseja excluir este cliente?')) {
        // Redefine os dados do cliente para vazios (simulando a exclusão)
        clienteAtual.name = 'N/A';
        clienteAtual.email = 'N/A';
        clienteAtual.cpf = 'N/A';
        clienteAtual.phone = 'N/A';
        
        // Atualiza a exibição na página
        displayClient();
        alert('Cliente excluído com sucesso!');
    }
});

// Evento de envio do formulário de edição
editForm.addEventListener('submit', (e) => {
    e.preventDefault(); // Impede o recarregamento da página

    // Atualiza o objeto cliente com os novos dados do formulário
    clienteAtual.name = editNameInput.value;
    clienteAtual.email = editEmailInput.value;
    clienteAtual.cpf = editCpfInput.value;
    clienteAtual.phone = editPhoneInput.value;

    // Esconde o formulário e mostra o perfil atualizado
    editForm.classList.add('d-none');
    profileContainer.classList.remove('d-none');
    displayClient();
    alert('Dados do cliente atualizados com sucesso!');
});

// Evento de clique para o botão Cancelar
cancelEditBtn.addEventListener('click', () => {
    // Esconde o formulário e mostra o perfil novamente
    editForm.classList.add('d-none');
    profileContainer.classList.remove('d-none');
});

// Chamada inicial para exibir os dados ao carregar a página
displayClient();