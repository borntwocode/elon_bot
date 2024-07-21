package uz.pdp.elonbot.messages;

public interface ValidationMessages {

    String INVALID_PHONE = "❌ Noto'g'ri telefon raqam: Iltimos, 9 ta raqamdan iborat raqam kiriting. Masalan: 908123456 📞";
    String INVALID_SCOOTER_TYPE = "❌ Noto'g'ri format. Iltimos, quyidagi tugmalardan birini tanlang: 🛴";
    String INVALID_SCOOTER_MODEL = "❌ Noto'g'ri format. Iltimos, skuter modelini to'g'ri kiriting. 🛵";
    String INVALID_MAX_SPEED = "❌ Noto'g'ri format. Kiritilgan qiymat faqat raqamdan iborat bo'lishi va 0-300 oralig'ida bo'lishi kerak. Masalan, 100 km/soat 🚀";
    String INVALID_ENGINE_POWER = "❌ Noto'g'ri format. Iltimos, motor quvvatini to'g'ri kiriting. ⚙️";
    String INVALID_RELEASED_YEAR = "❌ Noto'g'ri format. Kiritilgan qiymat 2000 yildan hozirgacha bo'lishi kerak. Masalan, 2022 📅";
    String INVALID_BATTERY_LIFE = "❌ Noto'g'ri format. Iltimos, akkumulyator hayotini to'g'ri kiriting. 🔋";
    String INVALID_KM_DRIVEN = "❌ Noto'g'ri format. Iltimos, bosib o'tgan yo'lni to'g'ri kiriting. 🌍";
    String INVALID_PRICE = "❌ Noto'g'ri format. Iltimos, narxni to'g'ri kiriting. 💰";
    String INVALID_ADDRESS = "❌ Noto'g'ri format. Iltimos, manzilni to'g'ri kiriting. 🏠";
    String INVALID_PHOTO = "❌ Noto'g'ri format. Rasmni fayl ko'rinishida jo'natmang, faqat 1 ta rasm jo'nating. 📸";

}
