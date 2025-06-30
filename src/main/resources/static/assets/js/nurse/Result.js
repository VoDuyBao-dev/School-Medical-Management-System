 document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('healthReportForm');
    const successAlert = document.getElementById('successAlert');
    const inputs = {
    studentName: document.getElementById('studentName'),
    visionResult: document.getElementById('visionResult'),
    hearingResult: document.getElementById('hearingResult'),
    bloodPressure: document.getElementById('bloodPressure'),
    heartRate: document.getElementById('heartRate'),
    height: document.getElementById('height'),
    weight: document.getElementById('weight'),
};
    const errors = {
    studentName: document.getElementById('studentNameError'),
    vision: document.getElementById('visionError'),
    hearing: document.getElementById('hearingError'),
    bloodPressure: document.getElementById('bloodPressureError'),
    heartRate: document.getElementById('heartRateError'),
    height: document.getElementById('heightError'),
    weight: document.getElementById('weightError'),
};

    // Regular expressions for validation
    const nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/; // Letters, Vietnamese diacritics, and spaces
    const hearingRegex = /^[a-zA-ZÀ-ỹ\s]{2,}$/; // Letters, diacritics, spaces, min 2 chars
    const bpRegex = /^(9[0-9]|1[0-7][0-9]|180)\/(6[0-9]|7[0-9]|8[0-9]|9[0-9]|1[0-1][0-9]|120)$/; // 90-180/60-120

    // Real-time validation
    inputs.studentName.addEventListener('input', () => {
    const value = inputs.studentName.value.trim();
    errors.studentName.style.display = value && nameRegex.test(value) ? 'none' : 'block';
});

    inputs.visionResult.addEventListener('input', () => {
    const value = parseFloat(inputs.visionResult.value);
    const isValid = !isNaN(value) && value >= 0 && value <= 10 && (value.toString().split('.')[1]?.length || 0) <= 1;
    errors.vision.style.display = isValid ? 'none' : 'block';
});

    inputs.hearingResult.addEventListener('input', () => {
    const value = inputs.hearingResult.value.trim();
    errors.hearing.style.display = value && hearingRegex.test(value) ? 'none' : 'block';
});

    inputs.bloodPressure.addEventListener('input', () => {
    const value = inputs.bloodPressure.value.trim();
    errors.bloodPressure.style.display = (value === '' || bpRegex.test(value)) ? 'none' : 'block';
});

    inputs.heartRate.addEventListener('input', () => {
    const value = parseInt(inputs.heartRate.value);
    errors.heartRate.style.display = (value >= 40 && value <= 200) || inputs.heartRate.value === '' ? 'none' : 'block';
});

    inputs.height.addEventListener('input', () => {
    const value = parseFloat(inputs.height.value);
    const isValid = (value >= 50 && value <= 250 && (value.toString().split('.')[1]?.length || 0) <= 1) || inputs.height.value === '';
    errors.height.style.display = isValid ? 'none' : 'block';
});

    inputs.weight.addEventListener('input', () => {
    const value = parseFloat(inputs.weight.value);
    const isValid = (value >= 10 && value <= 200 && (value.toString().split('.')[1]?.length || 0) <= 1) || inputs.weight.value === '';
    errors.weight.style.display = isValid ? 'none' : 'block';
});

    // Close alert functionality
    successAlert.querySelector('.close-btn').addEventListener('click', () => {
    successAlert.style.display = 'none';
});

    form.addEventListener('submit', (e) => {
    e.preventDefault();
    let isValid = true;

    // Validate required fields
    const studentName = inputs.studentName.value.trim();
    if (!studentName || !nameRegex.test(studentName)) {
    errors.studentName.style.display = 'block';
    isValid = false;
}

    const visionValue = parseFloat(inputs.visionResult.value);
    if (isNaN(visionValue) || visionValue < 0 || visionValue > 10 || (visionValue.toString().split('.')[1]?.length || 0) > 1) {
    errors.vision.style.display = 'block';
    isValid = false;
}

    const hearingValue = inputs.hearingResult.value.trim();
    if (!hearingValue || !hearingRegex.test(hearingValue)) {
    errors.hearing.style.display = 'block';
    isValid = false;
}

    const bloodPressureValue = inputs.bloodPressure.value.trim();
    if (bloodPressureValue && !bpRegex.test(bloodPressureValue)) {
    errors.bloodPressure.style.display = 'block';
    isValid = false;
}

    const heartRateValue = parseInt(inputs.heartRate.value);
    if (inputs.heartRate.value && (isNaN(heartRateValue) || heartRateValue < 40 || heartRateValue > 200)) {
    errors.heartRate.style.display = 'block';
    isValid = false;
}

    const heightValue = parseFloat(inputs.height.value);
    if (inputs.height.value && (isNaN(heightValue) || heightValue < 50 || heightValue > 250 || (heightValue.toString().split('.')[1]?.length || 0) > 1)) {
    errors.height.style.display = 'block';
    isValid = false;
}

    const weightValue = parseFloat(inputs.weight.value);
    if (inputs.weight.value && (isNaN(weightValue) || weightValue < 10 || weightValue > 200 || (weightValue.toString().split('.')[1]?.length || 0) > 1)) {
    errors.weight.style.display = 'block';
    isValid = false;
}

    if (isValid) {
    // Collect form data
    const formData = {
    studentName: inputs.studentName.value,
    studentId: document.getElementById('studentId').value || 'Chưa nhập',
    visionResult: inputs.visionResult.value,
    hearingResult: inputs.hearingResult.value,
    bloodPressure: inputs.bloodPressure.value || 'Chưa nhập',
    heartRate: inputs.heartRate.value || 'Chưa nhập',
    height: inputs.height.value || 'Chưa nhập',
    weight: inputs.weight.value || 'Chưa nhập',
    otherResult: document.getElementById('otherResult').value || 'Không có',
    assessment: document.getElementById('assessment').value || 'Không có',
    needsConsultation: document.getElementById('needsConsultation').checked ? 'Có' : 'Không',
    date: '26/06/2025 14:16 PM',
    reportCode: 'HC-2025-0626',
};

    // Save to localStorage
    const savedRecords = JSON.parse(localStorage.getItem('healthRecords') || '[]');
    savedRecords.push(formData);
    localStorage.setItem('healthRecords', JSON.stringify(savedRecords));

    // Show success alert
    successAlert.style.display = 'flex';

    // Reset form
    form.reset();
    Object.values(errors).forEach(error => error.style.display = 'none');
}
});
});