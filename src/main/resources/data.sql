INSERT INTO users (
    id,
    login_id,
    password,
    nickname,
    character_name,
    monthly_budget,
    monthly_saving_goal,
    fixed_expense,
    current_point,
    onboarding_completed,
    mission_notification,
    expense_record_notification,
    created_at
) VALUES (
    1,
    'testuser',
    '$2a$10$yjGmYS2ziF.DYNnYgDiK4.PQRtf/Iv/FThWDSLLdRblI9FqpwLxeC',
    '테스트유저',
    '말랑이',
    500000,
    100000,
    50000,
    1200,
    true,
    true,
    true,
    CURRENT_TIMESTAMP
);

INSERT INTO categories (id, code, name)
VALUES
    (1, 'FOOD', '식비'),
    (2, 'CAFE', '카페'),
    (3, 'TRANSPORT', '교통');

INSERT INTO rooms (id, user_id, background_image_url)
VALUES (1, 1, '/rooms/default-room.png');

INSERT INTO expenses (
    id,
    user_id,
    category_id,
    amount,
    memo,
    expense_date,
    created_at
) VALUES (
    1,
    1,
    1,
    12000,
    '점심',
    CURRENT_DATE,
    CURRENT_TIMESTAMP
);

INSERT INTO missions (
    id,
    user_id,
    mission_date,
    title,
    description,
    mission_type,
    status,
    progress,
    target_count,
    reward_point,
    point_received,
    target_category_code
) VALUES
    (
        1,
        1,
        CURRENT_DATE,
        '소비 기록하기',
        '오늘 소비를 1회 이상 기록해요.',
        'EXPENSE_RECORD',
        'COMPLETABLE',
        1,
        1,
        100,
        false,
        null
    ),
    (
        2,
        1,
        CURRENT_DATE,
        '오늘 예산 지키기',
        '오늘 사용 가능 금액 안에서 소비해요.',
        'UNDER_DAILY_BUDGET',
        'IN_PROGRESS',
        0,
        1,
        100,
        false,
        null
    );

INSERT INTO items (id, name, item_type, price, image_url)
VALUES
    (1, '원목 책상', 'FURNITURE', 5000, '/items/desk.png');

ALTER TABLE users ALTER COLUMN id RESTART WITH 2;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 4;
ALTER TABLE rooms ALTER COLUMN id RESTART WITH 2;
ALTER TABLE expenses ALTER COLUMN id RESTART WITH 2;
ALTER TABLE missions ALTER COLUMN id RESTART WITH 3;
ALTER TABLE items ALTER COLUMN id RESTART WITH 2;
