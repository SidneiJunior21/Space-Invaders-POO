import pygame
import random
from .entity import Entity
from .projectile import Projectile
import settings

class Enemy(Entity):
    def __init__(self, x, y, enemy_type, size):
        super().__init__()
        width, height = int(size[0]), int(size[1])
        self.image = pygame.Surface((width, height), pygame.SRCALPHA)

        if enemy_type == 'circle':
            pygame.draw.circle(self.image, settings.DEEPPINK, (width // 2, height // 2), width // 2)
        elif enemy_type == 'rect':
            self.image.fill(settings.AQUAMARINE)
        else:
            points = [(width // 2, 0), (0, height), (width, height)]
            pygame.draw.polygon(self.image, settings.ORANGE, points)
            
        self.rect = self.image.get_rect(topleft=(x, y))

class EnemyController:
    def __init__(self, screen_width, screen_height, all_sprites_group, enemy_size, enemy_spacing, rows, cols):
        self.enemies = pygame.sprite.Group()
        self.projectiles = pygame.sprite.Group()
        self.screen_width = screen_width
        self.screen_height = screen_height
        self.all_sprites = all_sprites_group
        self.direction = 1
        self.speed = 1
        self.drop_down_amount = enemy_spacing[1] / 2 
        
        self.create_enemies(enemy_size, enemy_spacing, rows, cols)

        self.last_enemy_shot_time = 0
        self.shot_cooldown = 800

    def create_enemies(self, enemy_size, enemy_spacing, rows, cols):
        for row in range(rows):
            for col in range(cols):
                x = (col * enemy_spacing[0]) + 50
                y = (row * enemy_spacing[1]) + 50
                
                if row == 0: enemy_type = 'circle'
                elif row in [1, 2]: enemy_type = 'rect'
                else: enemy_type = 'triangle'
                
                enemy = Enemy(x, y, enemy_type, enemy_size)
                self.enemies.add(enemy)
                self.all_sprites.add(enemy)
    
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
            projectile = Projectile(shooter.rect.centerx, shooter.rect.bottom, 4, settings.RED, self.screen_height)
            self.projectiles.add(projectile)
            self.all_sprites.add(projectile)