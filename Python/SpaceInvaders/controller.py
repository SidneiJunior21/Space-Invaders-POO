import pygame
import random
from sprites import Enemy, Projectile

class EnemyController:
    def __init__(self, screen_width, screen_height):
        self.enemies = pygame.sprite.Group()
        self.projectiles = pygame.sprite.Group()
        self.screen_width = screen_width
        self.direction = 1
        self.speed = 1
        self.drop_down_amount = 5
        self.create_enemies()

        self.last_enemy_shot_time = 0
        self.shot_cooldown = 800

    def create_enemies(self):
        for row in range(5):
            for col in range(8):
                x = (col * 60) + 50
                y = (row * 50) + 50
                
                if row == 0: enemy_type = 'circle'
                elif row in [1, 2]: enemy_type = 'rect'
                else: enemy_type = 'triangle'
                
                enemy = Enemy(x, y, enemy_type)
                self.enemies.add(enemy)
    
    def update(self):
        self.enemies.update()
        self.projectiles.update()

        self.move()
        self.shoot()
    
    def move(self):
        edge_reached = False
        for enemy in self.enemies:
            enemy.rect.x += self.speed * self.direction
            if enemy.rect.right >= self.screen_width or enemy.rect.left <= 0:
                edge_reached = True
        
        if edge_reached:
            self.direction *= -1
            for enemy in self.enemies:
                enemy.rect.y += self.drop_down_amount

    def shoot(self):
        now = pygame.time.get_ticks()
        if now - self.last_enemy_shot_time > self.shot_cooldown and self.enemies:
            self.last_enemy_shot_time = now
            shooter = random.choice(self.enemies.sprites())
            projectile = Projectile(shooter.rect.centerx, shooter.rect.bottom, 4, "red")
            self.projectiles.add(projectile)