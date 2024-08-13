document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById('offer-form');
    const candidateSelect = document.getElementById("candidate");
    const interviewSelect = document.getElementById("interview-info");
    const interviewNotes = document.getElementById("interview-notes");
    const interviewersList = document.getElementById("interviewers-list");
    const dueDateInput = document.getElementById('due-date');
    const contractPeriodStartInput = document.getElementById('contract-period-start');
    const contractPeriodEndInput = document.getElementById('contract-period-end');
    const dueDateError = document.getElementById('due-date-error');
    const contractPeriodEndError = document.getElementById('contract-period-end-error');
    const basicSalaryInput = document.getElementById('basic-salary'); // Assuming you have an input with id="basic-salary"
    const positionSelect = document.getElementById("position");
    // Format number with thousands separators
    function formatNumber(number) {
        return number.replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    }
    function removeSeparators(number) {
        return number.replace(/\./g, "");
    }
    function formatScientificNotation(value) {
        const number = parseFloat(value);
        if (!isNaN(number)) {
            return formatNumber(number.toFixed(0));
        }
        return value;
    }

    // Event listener for basic salary input
    basicSalaryInput.addEventListener('input', function (e) {
        const value = this.value.replace(/\./g, '');
        this.value = formatNumber(value);
    });

    console.log(interviewSchedulesByCandidateId);

    console.log(candidateSelect.value);
    if(candidateSelect.value) {
        const candidate = interviewSchedulesByCandidateId[candidateSelect.value]; // Assuming you have a candidatesById object
        if (candidate && candidate.position) {
            positionSelect.value = candidate.position;
        }
        const schedules = interviewSchedulesByCandidateId[candidateSelect.value] || [];
        console.log(schedules);
        basicSalaryInput.value = formatScientificNotation(basicSalaryInput.value);
        schedules.forEach(schedule => {
            const option = document.createElement('option');
            option.value = schedule.id;
            option.textContent = schedule.title;
            option.setAttribute('data-note', schedule.note || '');
            option.setAttribute('data-interviewers', schedule.interviewers.map(i => i.username).join(', '));
            interviewSelect.appendChild(option);
            interviewersList.innerHTML = 'Interviewers: ' + schedule.interviewers.map(i => i.username).join(', ');
        });
        const schedule = interviewSchedulesByCandidateId[candidateSelect.value]; // Assuming you have a candidatesById object
        console.log(schedule[0].candidate.position);
        positionSelect.value = schedule[0].candidate.position;
    }

    candidateSelect.addEventListener("change", function () {
        const candidateId = this.value;
        interviewSelect.innerHTML = '<option value="">Select an interview schedule title</option>';
        interviewNotes.value = '';
        interviewersList.innerHTML = 'Interviewers: ';

        if (candidateId && interviewSchedulesByCandidateId[candidateId]) {
            interviewSchedulesByCandidateId[candidateId].forEach(function (interview) {
                const option = document.createElement('option');
                option.value = interview.id;
                option.textContent = interview.title;
                option.setAttribute('data-note', interview.note || '');
                option.setAttribute('data-interviewers', interview.interviewers.map(i => i.username).join(', '));
                console.log(interview.note);
                console.log(interview.interviewers);
                interviewSelect.appendChild(option);
            });
            const schedule = interviewSchedulesByCandidateId[candidateSelect.value]; // Assuming you have a candidatesById object
            console.log(schedule[0].candidate.position);
            positionSelect.value = schedule[0].candidate.position;
        }
    });

    interviewSelect.addEventListener("change", function () {
        const selectedOption = interviewSelect.options[interviewSelect.selectedIndex];
        interviewNotes.value = selectedOption.getAttribute("data-note") || "";
        const interviewersData = selectedOption.getAttribute("data-interviewers");
        updateInterviewersList(interviewersData);
    });

    function updateInterviewersList(interviewersData) {
        interviewersList.innerHTML = "Interviewers: " + (interviewersData ? interviewersData : "None");
    }

    function validateDueDate() {
        const today = new Date().toISOString().split('T')[0];
        const dueDate = dueDateInput.value;
        if (dueDate && dueDate < today) {
            dueDateError.textContent = 'Due Date cannot be earlier than the current date.';
        } else {
            dueDateError.textContent = '';
        }
    }

    function validateContractPeriod() {
        const contractPeriodStart = new Date(contractPeriodStartInput.value);
        const contractPeriodEnd = new Date(contractPeriodEndInput.value);
        if (contractPeriodStartInput.value && contractPeriodEndInput.value) {
            if (contractPeriodStart >= contractPeriodEnd) {
                contractPeriodEndError.textContent = 'End Date must be later than Start Date.';
            } else {
                contractPeriodEndError.textContent = '';
            }
        }
    }

    dueDateInput.addEventListener('change', validateDueDate);
    contractPeriodEndInput.addEventListener('change', validateContractPeriod);

    form.addEventListener('submit', function (event) {
        basicSalaryInput.value = removeSeparators(basicSalaryInput.value);
        const today = new Date().toISOString().split('T')[0];
        const dueDate = new Date(dueDateInput.value);
        const contractPeriodStart = new Date(contractPeriodStartInput.value);
        const contractPeriodEnd = new Date(contractPeriodEndInput.value);

        if (dueDate < today) {
            alert('Due Date cannot be earlier than the current date.');
            event.preventDefault(); // Prevent form submission
            return;
        }

        if (contractPeriodStartInput.value && contractPeriodEndInput.value && contractPeriodStart >= contractPeriodEnd) {
            alert('End Date must be later than Start Date.');
            event.preventDefault(); // Prevent form submission
        }
    });
});
